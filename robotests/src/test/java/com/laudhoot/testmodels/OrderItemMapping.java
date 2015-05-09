package com.laudhoot.testmodels;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.laudhoot.persistence.model.BaseModel;

/**
 * Created by root on 7/3/15.
 */

@Table(name = "order_item_mapping")
public class OrderItemMapping extends BaseModel {

    @Column(name = "item")
    private Item item;

    @Column(name = "order")
    private Order order;

    public OrderItemMapping() {
    }

    public OrderItemMapping(Item item, Order order) {
        this.item = item;
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
