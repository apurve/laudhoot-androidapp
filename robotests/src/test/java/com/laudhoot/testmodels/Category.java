package com.laudhoot.testmodels;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.laudhoot.persistence.model.BaseModel;

import java.util.List;

/**
 * Created by apurve on 4/3/15.
 */

@Table(name = "categories")
public class Category extends BaseModel {

    @Column(name = "name")
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public List<Item> items() {
        return getMany(Item.class, "category");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}
