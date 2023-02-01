package com.es.phoneshop.security;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private Map<String, Long> timeMap = new HashMap<>();

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized boolean isAllowed(String ip) {
        System.out.println(ip);
        Long count = countMap.get(ip);
        Long time = System.currentTimeMillis();

        if (count == null) {
            count = 1L;
            timeMap.put(ip, time);
        } else {
            if (count > THRESHOLD) {
                return false;
            } else if (time - timeMap.get(ip) > 60000) {
                count = 0L;
                timeMap.put(ip, time);
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}