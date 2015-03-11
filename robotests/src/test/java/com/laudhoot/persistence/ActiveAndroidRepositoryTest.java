package com.laudhoot.persistence;

import com.laudhoot.RobolectricGradleTestRunner;
import com.laudhoot.testmodels.Category;
import com.laudhoot.testmodels.Item;
import com.laudhoot.testrepositories.CategoryRepository;
import com.laudhoot.testrepositories.ItemRepository;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by apurve on 7/3/15.
 */

@RunWith(RobolectricGradleTestRunner.class)
public class ActiveAndroidRepositoryTest {
    private CategoryRepository categoryRepository;
    private ItemRepository itemRepository;

    @Before
    public void setup(){
        categoryRepository = new CategoryRepository();
        itemRepository = new ItemRepository();
    }

    @Test
    public void sampleTest() throws Exception {
        Assert.assertTrue("Sample Test Successful", true);
    }

    @Test
    public void testOneToManyRelations() throws Exception {

        Category category1 = categoryRepository.createCategory("Category1");
        Category category2 = categoryRepository.createCategory("Category2");

        Item item1 = itemRepository.createItem("Item1", category1);
        Item item2 = itemRepository.createItem("Item2", category1);
        Item item3 = itemRepository.createItem("Item3", category1);

        Item item4 = itemRepository.createItem("Item4", category2);

        Assert.assertEquals("One-Many Test 1 Success : Correct no. of items retrieved from the category", category1.items().size(), 3);
        Assert.assertEquals("One-Many Test 2 Success : Correct no. of items retrieved from the category", category2.items().size(), 1);
    }
}
