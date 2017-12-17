package com.products.discounts.models;

import com.products.inventory.models.Product;

/**
 * Mandatory interface for any discount implementation
 */
public interface Discount {

    DiscountType getDiscountType();

    DiscountByType getDiscountByType();

    Double getDiscount();

    /**
     * The value to group similar discounts
     * Eg for Brand based Discounts, the key is the brand name
     *
     * @return
     */
    String getKey();

    /**
     * Check if this discount is applicable on the given product, for the given count
     *
     * @param product
     * @param count
     * @return
     */
    boolean isApplicable(Product product, Long count);


    /**
     * Method to provide the final discounted price for given product for given count
     *
     * @param product - product for which the discount is to be applied
     * @param count   - the no# of instance of this product
     * @return the final discounted price for this product & given count, as per this discount
     */
    default double apply(final Product product, final Long count) {
        switch (getDiscountByType()) {
            case PRICE:
                return product.getPrice() - getDiscount();
            case PERCENTAGE:
                return (100 - getDiscount()) * product.getPrice() / 100;
            default:
                throw new IllegalArgumentException("Invalid Discount By value");
        }
    }

}
