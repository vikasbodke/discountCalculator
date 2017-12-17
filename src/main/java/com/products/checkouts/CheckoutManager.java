package com.products.checkouts;

import com.products.checkouts.models.Cart;
import com.products.checkouts.models.Item;
import com.products.discounts.DiscountManager;
import com.products.discounts.models.Discount;
import com.products.inventory.ProductManager;
import com.products.inventory.models.Product;

import java.util.OptionalDouble;
import java.util.Set;


/**
 * Class helping in checkout activities
 * Needs the Product & Discount Managers for getting the respective details
 *
 * @author vikas
 */
public class CheckoutManager {

    private final ProductManager productManager;
    private final DiscountManager discountManager;


    public CheckoutManager(final DiscountManager discountManager, final ProductManager productManager) {
        this.productManager = productManager;
        this.discountManager = discountManager;
    }

    public long calculateTotalCost(final Cart cart) {
        final double totalCost = cart.getItems().stream()
                .mapToDouble(this::calculateTotalCost)
                .sum();

        //cart level discounts can be applied here
        return (long) totalCost;
    }

    public long calculateTotalCost(final Item item) {
        return (long) (getMinimumDiscountedPriceForItem(item) * item.getCount());
    }

    public double getMinimumDiscountedPriceForItem(final Item item) {
        final Product product = productManager.get(item.getProductId());
        return getMinimumDiscountedPriceForItem(product, item.getCount());
    }

    public double getMinimumDiscountedPriceForItem(final Product prod, final Long count) {
        final Set<Discount> discounts = discountManager.getApplicableDiscounts(prod, count);

        //iterate over the discounts and select the one with minimum price for this product & count
        OptionalDouble min = discounts.stream()
                .mapToDouble(discount -> discount.apply(prod, count))
                .min();
        return min.orElse(prod.getPrice());
    }
}
