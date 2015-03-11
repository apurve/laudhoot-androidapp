package com.laudhoot.testrepositories;

import com.laudhoot.persistence.ActiveAndroidRepository;
import com.laudhoot.testmodels.Category;
import com.laudhoot.testmodels.Item;

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
