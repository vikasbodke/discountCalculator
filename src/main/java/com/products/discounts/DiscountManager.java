package com.products.discounts;

import com.products.categorys.CategoryManager;
import com.products.discounts.models.Discount;
import com.products.discounts.models.DiscountType;
import com.products.inventory.models.Product;

import java.util.*;
import java.util.stream.Stream;

import static com.products.discounts.models.DiscountType.*;

/**
 * Class to manage all discount related activities, single point of truth
 */
public class DiscountManager {

    /**
     * Map to hold the available types of discount, to facilitate easy access/filtering
     */
    private final Map<DiscountType, Map<String, Set<Discount>>> discounts = new HashMap<>();

    /**
     * to trace the category hierarchy
     */
    private final CategoryManager categoryMgr;

    /**
     * @param categoryMgr - to trace the category hierarchy
     */
    public DiscountManager(CategoryManager categoryMgr) {
        this.categoryMgr = categoryMgr;

        //create an empty map for each type of discount
        Stream.of(values())
                .forEach(type -> discounts.put(type, new HashMap<>()));
    }


    /**
     * Clears all the discounts
     */
    public void clearAll() {
        this.discounts.values().forEach(Map::clear);
    }


    public DiscountManager addDiscount(Discount discount) {
        Map<String, Set<Discount>> discountMap = discounts.get(discount.getDiscountType());
        String key = discount.getKey();

        Set<Discount> discountsSet = discountMap.get(key);
        if (discountsSet == null) {
            //No discounts have been added for this brand/category yet
            discountsSet = new HashSet<>();
            discountsSet.add(discount);
            discountMap.put(key, discountsSet);
        } else {
            discountsSet.add(discount);
        }

        if (CATEGORY.equals(discount.getDiscountType())) {
            //need to calculate the discounts, as this update may have changed
            refreshAllCategoriesDiscount();
        }

        return this;
    }

    /**
     * Pre-calculate the discount for all categories-discount
     * This can be a background task, provided a lock is obtained on the category discounts
     */
    private void refreshAllCategoriesDiscount() {
        Map<String, Set<Discount>> categoryDiscounts = discounts.get(CATEGORY);
        //iterate over all the category discounts, filter & calculate the max discount
        categoryDiscounts.values()
                .forEach(set -> set.stream()
                        .map(discount -> (CategoryDiscount) discount)
                        .filter(CategoryDiscount::canInheritFromAncestor)
                        .forEach(cat -> cat.init(categoryMgr, categoryDiscounts)));
    }


    /**
     * Returns all the discounts which are applicable to this product with this count
     *
     * @param prod
     * @param count
     * @return
     */
    public Set<Discount> getApplicableDiscounts(Product prod, Long count) {
        Set<Discount> brandDiscounts = discounts.get(BRAND).get(prod.getBrandName());
        Set<Discount> categoryDiscounts = discounts.get(CATEGORY).get(prod.getCategory());

        Set<Discount> all = new HashSet<>();
        if (brandDiscounts != null)
            all.addAll(brandDiscounts);
        if (categoryDiscounts != null)
            all.addAll(categoryDiscounts);

        return Collections.unmodifiableSet(all);
    }

}
