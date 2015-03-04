package com.laudhoot.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by apurve on 4/3/15.
 */

@Table(name = "items")
public class Item extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private Category category;

    @Override
    public String toString() {
        return "Item{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category.toString() +
                '}';
    }

    public Item() {
    }

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
