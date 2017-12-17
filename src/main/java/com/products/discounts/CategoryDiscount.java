package com.products.discounts;

import com.products.discounts.models.Discount;
import com.products.discounts.models.DiscountByType;
import com.products.discounts.models.DiscountType;
import com.products.inventory.models.Product;

import static com.products.discounts.models.DiscountType.CATEGORY;


/**
 * @author vikas
 */
public class CategoryDiscount implements Discount {
    private final String category;
    private final Double discount;
    private final DiscountByType discountByType;

    /**
     * Flag to specify if this category can inherit from its ancestor
     */
    private final boolean canInheritFromAncestor;

    /**
     * this value will be enabled and used whenever the category can inherit discount from its parent
     */
    private Double maxHierarchyDiscount;

    /**
     * @param canInheritFromAncestor flag to indicate whether this category inherits the ancestors discount
     */
    public CategoryDiscount(final String category, final Double discount, final DiscountByType discountByType, final boolean canInheritFromAncestor) {
        this.category = category;
        this.discount = discount;
        this.discountByType = discountByType;
        this.canInheritFromAncestor = canInheritFromAncestor;
    }

    @Override
    public DiscountType getDiscountType() {
        return CATEGORY;
    }

    @Override
    public DiscountByType getDiscountByType() {
        return discountByType;
    }

    @Override
    public boolean isApplicable(Product product, Long count) {
        return category.equals(product.getCategory());
    }


    public boolean canInheritFromAncestor() {
        return canInheritFromAncestor;

    }

    public Double getMaxHierarchyDiscount() {
        return maxHierarchyDiscount;
    }

    public String getCategory() {
        return category;
    }

    public Double getDiscount() {
        return canInheritFromAncestor ? maxHierarchyDiscount : discount;
    }

    public Double getCategoryDiscount() {
        return discount;
    }

    @Override
    public String getKey() {
        return getCategory();
    }

    protected void setMaxHierarchyDiscount(double maxHierarchyDiscount) {
        this.maxHierarchyDiscount = maxHierarchyDiscount;
    }
}