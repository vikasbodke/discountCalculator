package com.products.categorys;

import com.products.categorys.models.Category;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage the Categories with their Hierarchy
 *
 * @author vikas
 */
public class CategoryManager {

    private final Map<String, Category> categories = new HashMap<>();

    public Category get(final String categoryName) {
        return categories.get(categoryName);
    }

    public CategoryManager add(final Category category) {
        categories.put(category.getName(), category);
        return this;
    }


    public CategoryManager remove(final Category category) {
        categories.remove(category.getName());
        return this;
    }
}
