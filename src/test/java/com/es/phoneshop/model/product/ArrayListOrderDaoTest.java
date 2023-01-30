package com.es.phoneshop.model.product;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {

    private OrderDao orderDao;

    @Mock
    private Order order;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductWithSameId() {
        String id = UUID.randomUUID().toString();
        when(order.getId()).thenReturn(Long.valueOf(id));
        orderDao.save(order);
        orderDao.save(order);
    }
}