package com.products.inventory;

import com.products.inventory.models.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage the Products
 *
 * @author vikas
 */
public class ProductManager {
    private final Map<Long, Product> inventory = new HashMap<>();

    public ProductManager add(final Product product) {
        inventory.put(product.getProductId(), product);
        return this;
    }

    public ProductManager remove(final Product product) {
        inventory.remove(product.getProductId());
        return this;
    }

    public ProductManager update(final Product product) {
        inventory.put(product.getProductId(), product);
        return this;
    }

    public Product get(final Long productId) {
        return inventory.get(productId);
    }
}
