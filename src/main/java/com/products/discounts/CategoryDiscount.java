package com.products.discounts;

import com.products.categorys.CategoryManager;
import com.products.categorys.models.Category;
import com.products.discounts.models.Discount;
import com.products.discounts.models.DiscountByType;
import com.products.discounts.models.DiscountType;
import com.products.inventory.models.Product;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

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
        return this.discountByType;
    }

    @Override
    public boolean isApplicable(Product product, Long count) {
        return category.equals(product.getCategory());
    }


    public boolean canInheritFromAncestor() {
        return canInheritFromAncestor;

    }

    protected void setMaxHierarchyDiscount(Double maxHierarchyDiscount) {
        this.maxHierarchyDiscount = maxHierarchyDiscount;
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

    /**
     * Logic to pre-calculate the discount in case this category is eligible for inheriting the ancestors discount
     *
     * @param categoryMgr       - to trace the ancestory
     * @param categoryDiscounts - to get the discount for a given category
     */
    public void init(CategoryManager categoryMgr, Map<String, Set<Discount>> categoryDiscounts) {
        if (!canInheritFromAncestor) {
            return;
        }

        String currCat = this.getCategory();
        double currMaxDiscount = getCategoryDiscount();
        while (currCat != null) {
            final Set<Discount> discounts = categoryDiscounts.get(currCat);
            if (discounts != null) {
                OptionalDouble maxDiscount = discounts.stream()
                        .map(discount -> (CategoryDiscount) discount)
                        .mapToDouble(CategoryDiscount::getCategoryDiscount).max();
                currMaxDiscount = Math.max(currMaxDiscount, maxDiscount.orElse(0.0));
            }
            Category cat = categoryMgr.get(currCat);
            currCat = cat.getParent() != null ? cat.getParent().getName() : null;
        }

        maxHierarchyDiscount = currMaxDiscount;
    }
}