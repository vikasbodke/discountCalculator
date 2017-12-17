package com.products.checkouts.models;

import java.util.Collection;
import java.util.Set;

/**
 * Class representing a customer items in cart
 *
 * @author vikas
 */
public class Cart {

    private final Set<Item> items;

    public Cart(Set<Item> items) {
        this.items = items;
    }

    //Fluent apis to add/update items
    public Cart addItem(final Item item) {
        this.items.add(item);
        return this;
    }

    public Cart addItem(final Collection<Item> items) {
        this.items.addAll(items);
        return this;
    }

    public Cart removeItem(final Item item) {
        this.items.remove(item);
        return this;
    }

    public Cart removeItems(final Collection<Item> items) {
        this.items.removeAll(items);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }
}
