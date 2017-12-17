package com.products.inventory.models;

import java.util.Objects;

/**
 * Class representing a product entity
 *
 * @author vikas
 */
public class Product {
    //ASSUMPTION: productId is a simple integer & within the range
    private final Long productId;

    //ASSUMPTION: the value will be treated as unique key
    private final String brandName;

    //ASSUMPTION: the value will be treated as unique key
    private final String category;

    //ASSUMPTION: long, not double
    //price value is consistent and not dynamic
    private final Long price;

    public Product(Long productId, String brandName, String category, Long price) {
        this.productId = productId;
        this.brandName = brandName;
        this.category = category;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getCategory() {
        return category;
    }

    public Long getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getProductId(), product.getProductId()) &&
                Objects.equals(getBrandName(), product.getBrandName()) &&
                Objects.equals(getCategory(), product.getCategory()) &&
                Objects.equals(getPrice(), product.getPrice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProductId(), getBrandName(), getCategory(), getPrice());
    }
}
