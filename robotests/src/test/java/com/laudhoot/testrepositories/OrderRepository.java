package com.laudhoot.testrepositories;

import com.laudhoot.persistence.repository.ActiveAndroidRepository;
import com.laudhoot.testmodels.Item;
import com.laudhoot.testmodels.Order;
import com.laudhoot.testmodels.OrderItemMapping;

import java.util.List;

/**
 * Created by root on 7/3/15.
 */
public class OrderRepository extends ActiveAndroidRepository {

    public OrderRepository() {
        super(Order.class);
    }

    public Order createOrder(String name, List<Item> items) {
        Order order = new Order(name);
        saveOrUpdate(order);
        for (Item item : items) {
            new OrderItemMapping(item, order).save();
        }
        return order;
    }

}
