package com.es.phoneshop.model.order;

import com.es.phoneshop.model.product.*;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListOrderDao implements OrderDao {
    private long orderId;
    private List<Order> orders;

    private ArrayListOrderDao() {
        orders = new ArrayList<>();
    }

    private static class SingletonHelper {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return SingletonHelper.INSTANCE;
    }
    @Override
    public synchronized Order getOrder(Long id) throws OrderNotFoundException {
        return orders.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void save(Order order) throws OrderNotFoundException {
        Long id = order.getId();
        if (id != null) {
            orders.remove(getOrder(id));
            orders.add(order);
        } else {
            order.setId(++orderId);
            orders.add(order);
        }
    }

    @Override
    public synchronized Order getOrderBySecureId(String id) throws OrderNotFoundException {
        return orders.stream()
                .filter(product -> id.equals(product.getSecureId()))
                .findAny()
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}