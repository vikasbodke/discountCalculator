package com.products.discounts;

import com.products.categorys.CategoryManager;
import com.products.categorys.models.Category;
import com.products.discounts.models.Discount;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * Utility class to calculate the max discount for a category
 *
 * @author vikas
 */
public class CategoryHierarchyDiscountUtil {
    /**
     * Logic to pre-calculate the discount in case this category is eligible for inheriting the ancestors discount
     *
     * @param category          - the category to calculate the discount for
     * @param categoryMgr       - to trace the ancestory
     * @param categoryDiscounts - to get the discount for a given category
     */

    public static void calculateMaxDiscount(CategoryDiscount category, CategoryManager categoryMgr, Map<String, Set<Discount>> categoryDiscounts) {
        if (!category.canInheritFromAncestor()) {
            return;
        }

        String currCat = category.getCategory();
        //seeding with current category's discount
        double currMaxDiscount = category.getCategoryDiscount();

        //trace the hierarchy upward, calculating the max discount at each level
        while (currCat != null) {
            final Set<Discount> discounts = categoryDiscounts.get(currCat);
            if (discounts != null) {
                //Get the max of all the discounts available for this category
                OptionalDouble maxDiscount = discounts.stream()
                        .map(discount -> (CategoryDiscount) discount)
                        .mapToDouble(CategoryDiscount::getCategoryDiscount).max();
                currMaxDiscount = Math.max(currMaxDiscount, maxDiscount.orElse(0.0));
            }

            //get the parent category for current category
            Category cat = categoryMgr.get(currCat);
            currCat = cat.getParent() != null ? cat.getParent().getName() : null;
        }

        category.setMaxHierarchyDiscount(currMaxDiscount);
    }

}
