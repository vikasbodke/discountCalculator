package com.products.checkouts.models;

import java.util.Objects;

/**
 * Class representing a single product & its count in a cart
 *
 * @author vikas
 */
public class Item {
    private final Long productId;
    private final Long count;

    public Item(final Long productId, final Long count) {
        this.productId = productId;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getProductId(), item.getProductId()) &&
                Objects.equals(getCount(), item.getCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId());
    }
}
