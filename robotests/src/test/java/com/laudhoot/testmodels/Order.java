package com.laudhoot.testmodels;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.laudhoot.model.BaseModel;

import java.util.List;

/**
 * Created by apurve on 6/3/15.
 */

@Table(name = "orders")
public class Order extends BaseModel {

    @Column(name = "name")
    private String name;

    public Order() {
    }

    public List<Item> getItems(){
        return getManyThroughMapping(Item.class, OrderItemMapping.class);
    }

    public Order(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}
