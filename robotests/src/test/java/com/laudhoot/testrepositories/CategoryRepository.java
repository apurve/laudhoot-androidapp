package com.laudhoot.testrepositories;

import com.laudhoot.persistence.repository.ActiveAndroidRepository;
import com.laudhoot.testmodels.Category;

/**
 * Created by root on 4/3/15.
 */
public class CategoryRepository extends ActiveAndroidRepository {

    public CategoryRepository() {
        super(Category.class);
    }

    public Category createCategory(String name) {
        Category category = new Category(name);
        saveOrUpdate(category);
        return category;
    }

}
