package com.products.categorys.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a node in the Categories hierarchy
 *
 * @author vikas
 */

public class Category {

    private final String name;
    private final Category parent;
    private final List<Category> children;

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public Category getParent() {
        return parent;
    }

    public Category addChild(Category cat) {
        this.children.add(cat);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Category> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(getName(), category.getName()) &&
                Objects.equals(getChildren(), category.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getChildren());
    }
}
