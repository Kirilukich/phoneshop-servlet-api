package com.es.phoneshop.security;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private final ArrayList<ScheduledExecutorService> schedulerList = new ArrayList<>();

    private final Runnable runnable = new Runnable() {
        int start = 60;
        @Override
        public void run() {
            start--;
            if (start < 0) {
                schedulerList.get(0).shutdown();
                start = 60;
            }
        }
    };

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized boolean isAllowed(String ip) {
        schedulerList.add(Executors.newScheduledThreadPool(1));
        Long count = countMap.get(ip);
        if (count == null || count == 0) {
            count = 1L;
            schedulerList.get(0).scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        } else {
            if (count > THRESHOLD && !schedulerList.get(0).isShutdown()) {
                return false;
            } else if (count <= THRESHOLD && schedulerList.get(0).isShutdown()) {
                count = -1L;
                schedulerList.set(0, Executors.newScheduledThreadPool(1));
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}