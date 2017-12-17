package com.products;


import com.products.checkouts.CheckoutManager;
import com.products.checkouts.models.Cart;
import com.products.checkouts.models.Item;
import com.products.discounts.DiscountManager;
import com.products.inventory.ProductManager;
import com.products.inventory.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoDiscountScenario {

    private List<Product> products = new ArrayList<>();
    private CheckoutManager checkoutMgr;

    @BeforeAll
    private void init() {
        final Product product = new Product(1L, "Adidas", "Casuals", 1000L);
        products.add(product);
        DiscountManager discountMgr = Mockito.mock(DiscountManager.class);
        ProductManager productMgr = Mockito.mock(ProductManager.class);
        checkoutMgr = new CheckoutManager(discountMgr, productMgr);

        //mock mappings
        when(productMgr.get(any())).thenReturn(product);
        when(discountMgr.getApplicableDiscounts(any(), any())).thenReturn(emptySet());
    }


    @Test
    public void test_SingleProduct() {
        double discountedPrice = checkoutMgr.getMinimumDiscountedPriceForItem(products.get(0), 1L);
        assertEquals((long) discountedPrice, (long) products.get(0).getPrice());
    }

    @Test
    public void test_SingleItem() {
        Item item = new Item(products.get(0).getProductId(), 1L);
        double discountedPrice = checkoutMgr.getMinimumDiscountedPriceForItem(item);
        assertEquals((long) discountedPrice, (long) products.get(0).getPrice());
    }

    @Test
    public void test_SingleCart() {
        Item item = new Item(products.get(0).getProductId(), 1L);
        Cart cartWithOneItem = new Cart(Collections.singleton(item));
        double discountedPrice = checkoutMgr.calculateTotalCost(cartWithOneItem);
        assertEquals((long) discountedPrice, (long) products.get(0).getPrice());
    }


}
