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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.products.discounts.models.DiscountByType.PERCENTAGE;
import static java.lang.Long.parseLong;

/**
 * @author vikas
 */

public class App {

    private static final String VALUE_SEPARATOR = ",";

    public static void main(String[] args) {
        //read the no# of products
        Scanner stdScanner = new Scanner(System.in);
        int inventoryCount = stdScanner.nextInt();
        stdScanner.nextLine();

        //read the products & initialize into the product repository
        ProductManager productMgr = new ProductManager();
        for (int i = 0; i < inventoryCount; i++) {
            String[] productArgs = stdScanner.nextLine().split(VALUE_SEPARATOR);
            Product product = new Product(parseLong(productArgs[0]), productArgs[1].trim(), productArgs[2].trim(), parseLong(productArgs[3]));
            productMgr.add(product);
        }
        stdScanner.nextLine();

        //read the no# of customers
        int noOfCustomer = stdScanner.nextInt();
        stdScanner.nextLine();

        //read the customer items & initialize them into carts
        List<Cart> carts = new ArrayList<>(noOfCustomer);
        for (int i = 0; i < noOfCustomer; i++) {
            final String line = stdScanner.nextLine();
            Set<Item> items = Stream.of(line.split(VALUE_SEPARATOR))
                    .mapToLong(Integer::parseInt)
                    .mapToObj(val -> new Item(val, 1L))
                    .collect(Collectors.toSet());
            carts.add(new Cart(items));
        }

        //Initialize the discounts & category hierarchies
        CategoryManager categoryMgr = new CategoryManager();
        DiscountManager discountMgr = new DiscountManager(categoryMgr);
        CheckoutManager checkoutMgr = new CheckoutManager(discountMgr, productMgr);
        init(discountMgr, categoryMgr);

        //for each customer cart, calculate the discount
        carts.stream()
                .map(checkoutMgr::calculateTotalCost)
                .forEach(System.out::println);
    }


    private static void init(DiscountManager discountMgr, CategoryManager catMgr) {
        //Set up the categories hierarchy
        //Men's Wear Categories
        Category mensWear = new Category("Men's Wear", null);
        catMgr.add(mensWear);
        Stream.of("Shirts", "Trousers", "Casuals", "Jeans")
                .map(categoryName -> new Category(categoryName, mensWear))
                .forEach(catMgr::add);

        //Women's Wear Categories
        Category womensWear = new Category("Women's wear", null);
        catMgr.add(womensWear);
        Stream.of("Dresses", "Footwear")
                .map(categoryName -> new Category(categoryName, womensWear))
                .forEach(catMgr::add);


        //set up the discount by category
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

    }

}
