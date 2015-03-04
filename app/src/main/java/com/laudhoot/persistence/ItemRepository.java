package com.laudhoot.persistence;

import android.util.Log;

import com.laudhoot.Laudhoot;
import com.laudhoot.model.Category;
import com.laudhoot.model.Item;

/**
 * Created by apurve on 4/3/15.
 */
public class ItemRepository extends ActiveAndroidRepository {

    public ItemRepository() {
        super(Item.class);
    }

    public Item createItem(String name, Category category){
        if(category.getId() != null){
            Item item = new Item(name, category);
            saveOrUpdate(item);
            return item;
        }else{
            throw new IllegalArgumentException("Can't save item to non-persisted category.");
        }
    }

}
