package com.products.discounts;

import com.products.discounts.models.Discount;
import com.products.discounts.models.DiscountByType;
import com.products.discounts.models.DiscountType;
import com.products.inventory.models.Product;

/**
 * @author vikas
 */
public class BrandDiscount implements Discount {
    private final String brandName;
    private final Double discount;
    private final DiscountByType discountByType;

    public BrandDiscount(final String brandName, final Double discount, final DiscountByType discountByType) {
        this.brandName = brandName;
        this.discount = discount;
        this.discountByType = discountByType;
    }

    @Override
    public DiscountType getDiscountType() {
        return DiscountType.BRAND;
    }

    @Override
    public DiscountByType getDiscountByType() {
        return this.discountByType;
    }

    @Override
    public boolean isApplicable(Product product, Long count) {
        return brandName.equals(product.getBrandName());
    }

    public String getBrandName() {
        return brandName;
    }

    public Double getDiscount() {
        return discount;
    }

    @Override
    public String getKey() {
        return getBrandName();
    }
}