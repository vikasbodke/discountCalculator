package com.products;

import com.products.checkouts.CheckoutManager;
import com.products.checkouts.models.Cart;
import com.products.checkouts.models.Item;
import com.products.discounts.BrandDiscount;
import com.products.discounts.CategoryDiscount;
import com.products.discounts.DiscountManager;
import com.products.discounts.models.Discount;
import com.products.inventory.ProductManager;
import com.products.inventory.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.products.discounts.models.DiscountByType.PERCENTAGE;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryBrandDiscountScenario {
    private List<Product> products = new ArrayList<>();
    private CheckoutManager checkoutMgr;

    @BeforeAll
    private void init() {
        final Product product_1 = new Product(1L, "Adidas", "Casuals", 1000L);
        final Product product_2 = new Product(2L, "Wrangler", "Footwear", 1000L);

        products.add(product_1);
        products.add(product_2);
        DiscountManager discountMgr = Mockito.mock(DiscountManager.class);
        ProductManager productMgr = Mockito.mock(ProductManager.class);
        checkoutMgr = new CheckoutManager(discountMgr, productMgr);

        //mock mappings
        when(productMgr.get(eq(1L))).thenReturn(product_1);
        when(productMgr.get(eq(2L))).thenReturn(product_2);

        Discount brandDiscount = new BrandDiscount("Aiddas", 20.0, PERCENTAGE);
        Discount categoryDiscount = new CategoryDiscount("Footwear", 50.0, PERCENTAGE, false);

        when(discountMgr.getApplicableDiscounts(eq(product_1), any())).thenReturn(singleton(brandDiscount));
        when(discountMgr.getApplicableDiscounts(eq(product_2), any())).thenReturn(singleton(categoryDiscount));
    }

    @Test
    public void test_SingleProduct() {
        double discountedPrice = checkoutMgr.getMinimumDiscountedPriceForItem(products.get(0), 1L);
        assertEquals((long) discountedPrice, products.get(0).getPrice() * .8);
    }

    @Test
    public void test_SingleItem() {
        Item item = new Item(products.get(0).getProductId(), 1L);
        double discountedPrice = checkoutMgr.getMinimumDiscountedPriceForItem(item);
        assertEquals((long) discountedPrice, products.get(0).getPrice() * .8);
    }

    @Test
    public void test_SingleCart() {
        Cart cart = new Cart(new HashSet<>());
        cart.addItem(new Item(products.get(0).getProductId(), 1L));
        cart.addItem(new Item(products.get(1).getProductId(), 2L));

        double discountedPrice = checkoutMgr.calculateTotalCost(cart);
        assertEquals((long) discountedPrice, (long) 1800);
    }

}
