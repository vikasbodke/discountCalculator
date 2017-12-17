package com.products;

import com.products.categorys.CategoryManager;
import com.products.categorys.models.Category;
import com.products.checkouts.CheckoutManager;
import com.products.checkouts.models.Cart;
import com.products.checkouts.models.Item;
import com.products.discounts.BrandDiscount;
import com.products.discounts.CategoryDiscount;
import com.products.discounts.DiscountManager;
import com.products.inventory.ProductManager;
import com.products.inventory.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.products.discounts.models.DiscountByType.PERCENTAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProblemExample {

    private CheckoutManager checkoutMgr;

    @BeforeAll
    private void init() {

        ProductManager productMgr = new ProductManager();
        CategoryManager categoryMgr = new CategoryManager();

        //set up the categories hierarchy
        Category mensWear = new Category("Men's Wear", null);
        categoryMgr.add(mensWear);
        Stream.of("Shirts", "Trousers", "Casuals", "Jeans")
                .map(categoryName -> new Category(categoryName, mensWear))
                .forEach(categoryMgr::add);

        Category womensWear = new Category("Women's wear", null);
        categoryMgr.add(womensWear);
        Stream.of("Dresses", "Footwear")
                .map(categoryName -> new Category(categoryName, womensWear))
                .forEach(categoryMgr::add);


        //set up the discount by category
        DiscountManager discountMgr = new DiscountManager(categoryMgr);
        discountMgr.addDiscount(new CategoryDiscount("Men's Wear", 0.0, PERCENTAGE, false))
                .addDiscount(new CategoryDiscount("Shirts", 0.0, PERCENTAGE, true))
                .addDiscount(new CategoryDiscount("Trousers", 30.0, PERCENTAGE, true))
                .addDiscount(new CategoryDiscount("Casuals", 30.0, PERCENTAGE, true))
                .addDiscount(new CategoryDiscount("Jeans", 20.0, PERCENTAGE, true));

        discountMgr.addDiscount(new CategoryDiscount("Women's wear", 50.0, PERCENTAGE, false))
                .addDiscount(new CategoryDiscount("Footwear", 0.0, PERCENTAGE, true))
                .addDiscount(new CategoryDiscount("Dresses", 0.0, PERCENTAGE, true));

        //set up the discount by brands
        discountMgr.addDiscount(new BrandDiscount("Wrangler", 10.0, PERCENTAGE))
                .addDiscount(new BrandDiscount("Arrow", 20.0, PERCENTAGE))
                .addDiscount(new BrandDiscount("Vero Moda", 60.0, PERCENTAGE))
                .addDiscount(new BrandDiscount("Adidas", 5.0, PERCENTAGE))
                .addDiscount(new BrandDiscount("Provogue", 20.0, PERCENTAGE));


        //setup inventory
        productMgr.add(new Product(1L, "Arrow", "Shirts", 800L))
                .add(new Product(2L, "Vero Moda", "Dresses", 1400L))
                .add(new Product(3L, "Provogue", "Footwear", 1800L))
                .add(new Product(4L, "Wrangler", "Jeans", 2200L))
                .add(new Product(5L, "UCB", "Shirts", 1500L));

        checkoutMgr = new CheckoutManager(discountMgr, productMgr);
    }


    @Test
    public void test_Customer1() {
        Set<Item> items = Stream.of(1, 2, 3, 4)
                .mapToLong(Long::valueOf)
                .mapToObj(val -> new Item(val, 1L))
                .collect(Collectors.toSet());
        Cart cart = new Cart(items);

        double discountedPrice = checkoutMgr.calculateTotalCost(cart);
        assertEquals((long) 3860, (long) discountedPrice);
    }

    @Test
    public void test_Customer2() {
        Set<Item> items = Stream.of(1, 5)
                .mapToLong(Long::valueOf)
                .mapToObj(val -> new Item(val, 1L))
                .collect(Collectors.toSet());
        Cart cart = new Cart(items);

        double discountedPrice = checkoutMgr.calculateTotalCost(cart);
        assertEquals((long) 2140, (long) discountedPrice);
    }

    @Test
    public void test_Customer3() {
        Set<Item> items = Stream.of(1, 2, 3, 4, 5)
                .mapToLong(Long::valueOf)
                .mapToObj(val -> new Item(val, 1L))
                .collect(Collectors.toSet());
        Cart cart = new Cart(items);

        double discountedPrice = checkoutMgr.calculateTotalCost(cart);
        assertEquals((long) 5360, (long) discountedPrice);
    }

}
