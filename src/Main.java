// ASSUMPTION: All taxes (e.g. sales, extra) for a single item are combined and calculated only once.
// ASSUMPTION: All items in a bundle will have the same Bundle object added to their ItemSchemes.
// ASSUMPTION: All items in a bundle will have the BUNDLE PricingCategory type.

import java.util.HashMap;

public class Main {
    // DO have regular sales tax
    private static final String MILK_ID = "8873";       // SIMPLE
    private static final String TOOTHBRUSH_ID = "1983"; // BUY_X_GET_Y
    private static final String WINE_ID = "0923";       // EXTRA_TAX
    private static final String CHIPS_ID = "6732";      // BUNDLE
    private static final String SALSA_ID = "4900";      // BUNDLE
    private static final String TOOTHPASTE_ID = "3874"; // BUY_X_GET_Y

    // DO NOT have regular sales tax
    private static final String BANANA_ID = "7777";     // BUY_X_GET_Y
    private static final String PUMPKIN_ID = "6548";    // SIMPLE
    private static final String SHIRT_ID = "6841";      // BUNDLE
    private static final String JACKET_ID = "2588";     // BUNDLE
    private static final String ANTACID_ID = "4949";    // EXTRA_TAX

    private static final String MYSTERY_ITEM_ID = "1234"; // no ItemScheme will exist for this item

    public static void main(String[] args) {
        HashMap itemSchemes = new HashMap<String, ItemScheme>();
        double dollars;
        int numItemsPurchased;
        String assertFailMessageTotal = "Sale invalid. Error with total cost.";
        String assertFailMessageCount = "Sale invalid. Error with item count.";

        // Create item schemes for all items that can be purchased today. (Assumes small, predefined store selection.)
        // MILK
        ItemScheme milkScheme = new ItemScheme(MILK_ID, 2.49, PricingCategory.SIMPLE, true);
        // TOOTHBRUSH
        ItemScheme toothbrushScheme = new ItemScheme(TOOTHBRUSH_ID, 1.99, PricingCategory.BUY_X_GET_Y, true);
        toothbrushScheme.setBuyXGetYDeal(2, 1);
        // WINE
        ItemScheme wineScheme = new ItemScheme(WINE_ID, 15.49, PricingCategory.EXTRA_TAX, true);
        wineScheme.setExtraTax(0.0925);
        // CHIPS & SALSA
        ItemScheme chipsScheme = new ItemScheme(CHIPS_ID, 2.49, PricingCategory.BUNDLE, true);
        ItemScheme salsaScheme = new ItemScheme(SALSA_ID, 3.49, PricingCategory.BUNDLE, true);
        Bundle fiestaPromo = new Bundle(new String[]{CHIPS_ID, SALSA_ID}, 4.99); // chips & salsa bundle
        chipsScheme.addBundle(fiestaPromo);
        salsaScheme.addBundle(fiestaPromo);
        // TOOTHPASTE
        ItemScheme toothpasteScheme = new ItemScheme(TOOTHPASTE_ID, 0.95, PricingCategory.BUY_X_GET_Y, true);
        toothpasteScheme.setBuyXGetYDeal(3, 5);

        // Create item schemes for tomorrow's pricing scheme.
        // WINE (50% OFF)
        ItemScheme wineHalfOffScheme = new ItemScheme(WINE_ID, 7.75, PricingCategory.EXTRA_TAX, true);
        wineHalfOffScheme.setExtraTax(0.0925);
        // BANANA
        ItemScheme bananaScheme = new ItemScheme(BANANA_ID, 0.79, PricingCategory.BUY_X_GET_Y, false);
        bananaScheme.setBuyXGetYDeal(2,2);
        // PUMPKIN
        ItemScheme pumpkinScheme = new ItemScheme(PUMPKIN_ID, 3.99, PricingCategory.SIMPLE, false);
        // SHIRT & JACKET
        ItemScheme shirtScheme = new ItemScheme(SHIRT_ID, 24.99, PricingCategory.BUNDLE, false);
        ItemScheme jacketScheme = new ItemScheme(JACKET_ID, 89.99, PricingCategory.BUNDLE, false);
        Bundle dressCoolPromo = new Bundle(new String[]{SHIRT_ID, JACKET_ID}, 100); // shirt & jacket bundle
        shirtScheme.addBundle(dressCoolPromo);
        jacketScheme.addBundle(dressCoolPromo);
        // ANTACID
        ItemScheme antacidScheme = new ItemScheme(ANTACID_ID, 12.99, PricingCategory.EXTRA_TAX, false);
        antacidScheme.setExtraTax(0.04);

        // BUILD TODAY'S PRICING SCHEME (PROVIDED SCENARIO)
        itemSchemes.put(MILK_ID, milkScheme);
        itemSchemes.put(TOOTHBRUSH_ID, toothbrushScheme);
        itemSchemes.put(WINE_ID, wineScheme);
        itemSchemes.put(CHIPS_ID, chipsScheme);
        itemSchemes.put(SALSA_ID, salsaScheme);
        itemSchemes.put(TOOTHPASTE_ID, toothpasteScheme);
        PricingScheme todaysScheme = new PricingScheme(itemSchemes, 0.00); // assuming a 0% sales tax to match provided example scenario

        // Check out customer #1
        Checkout c = new Checkout(todaysScheme);
        c.scan(TOOTHBRUSH_ID);  // toothbrush
        c.scan(SALSA_ID);       // salsa
        c.scan(MILK_ID);        // milk
        c.scan(CHIPS_ID);       // chips
        c.scan(WINE_ID);        // wine
        c.scan(TOOTHBRUSH_ID);  // toothbrush
        c.scan(TOOTHBRUSH_ID);  // toothbrush
        c.scan(TOOTHBRUSH_ID);  // toothbrush

        // BUILD TOMORROW'S PRICING SCHEME (now with a non-zero tax rate)
        // base price of wine is now 50% off!
        itemSchemes.put(WINE_ID, wineHalfOffScheme); // replaces the previous wine pricing scheme
        // new items added to the store: banana, pumpkin, shirt, jacket, antacid (regular sales tax not charged on these)
        itemSchemes.put(BANANA_ID, bananaScheme);
        itemSchemes.put(PUMPKIN_ID, pumpkinScheme);
        itemSchemes.put(SHIRT_ID, shirtScheme);
        itemSchemes.put(JACKET_ID, jacketScheme);
        itemSchemes.put(ANTACID_ID, antacidScheme);
        PricingScheme tomorrowsScheme = new PricingScheme(itemSchemes, 0.06); // assuming a 6% sales tax

        // Check out customer #2 (with tomorrow's pricing scheme)
        Checkout dentistCheckout = new Checkout(tomorrowsScheme);
        for (int i = 0; i < 123; i++) {
            dentistCheckout.scan(TOOTHBRUSH_ID);
        }
        for (int i = 0; i < 99; i++) {
            dentistCheckout.scan(TOOTHPASTE_ID);
        }

        // Check out customer #3 (with tomorrow's pricing scheme)
        Checkout shoppingSpree = new Checkout(tomorrowsScheme);
        shoppingSpree.scan(TOOTHBRUSH_ID);
        shoppingSpree.scan(TOOTHBRUSH_ID);
        shoppingSpree.scan(TOOTHBRUSH_ID);
        shoppingSpree.scan(MILK_ID);
        shoppingSpree.scan(BANANA_ID);
        shoppingSpree.scan(BANANA_ID);
        shoppingSpree.scan(BANANA_ID);
        shoppingSpree.scan(PUMPKIN_ID);
        shoppingSpree.scan(MYSTERY_ITEM_ID); // this item is ignored because it does not have an item scheme
        shoppingSpree.scan(JACKET_ID);
        shoppingSpree.scan(JACKET_ID);
        shoppingSpree.scan(JACKET_ID);
        shoppingSpree.scan(JACKET_ID);
        shoppingSpree.scan(SHIRT_ID);
        shoppingSpree.scan(SHIRT_ID);
        shoppingSpree.scan(MYSTERY_ITEM_ID); // this item is ignored because it does not have an item scheme
        shoppingSpree.scan(SHIRT_ID);
        shoppingSpree.scan(SHIRT_ID);
        shoppingSpree.scan(SHIRT_ID);
        shoppingSpree.scan(SHIRT_ID);
        shoppingSpree.scan(CHIPS_ID);
        shoppingSpree.scan(CHIPS_ID);
        shoppingSpree.scan(SALSA_ID);
        shoppingSpree.scan(ANTACID_ID);
        for (int i = 0; i < 20; i++) {
            shoppingSpree.scan(WINE_ID);
        }

        // Print test results from all customers
        System.out.println("\n============RESULTS============\n");
        System.out.println("*PROVIDED SCENARIO*");
        dollars = convertToDollarsCents(c.getTotal());
        numItemsPurchased = c.getItemsPaidFor();
        prettyPrint(numItemsPurchased, dollars);
        assert (numItemsPurchased == 8) : assertFailMessageCount;
        assert (dollars == 30.37) : assertFailMessageTotal;

        System.out.println("*DENTIST SCENARIO*");
        dollars = convertToDollarsCents(dentistCheckout.getTotal());
        numItemsPurchased = dentistCheckout.getItemsPaidFor();
        prettyPrint(numItemsPurchased, dollars);
        assert (numItemsPurchased == 222) : assertFailMessageCount;
        assert (dollars == 212.24) : assertFailMessageTotal;

        System.out.println("*SHOPPING SPREE SCENARIO*");
        dollars = convertToDollarsCents(shoppingSpree.getTotal());
        numItemsPurchased = shoppingSpree.getItemsPaidFor();
        prettyPrint(numItemsPurchased, dollars);
        assert (numItemsPurchased == 42) : assertFailMessageCount;
        assert (dollars == 662.48) : assertFailMessageTotal;
    }

    // Truncates given value to 2 decimal places.
    private static double convertToDollarsCents (double dollars) {
        return Math.floor(dollars * 100) / 100;
    }

    private static void prettyPrint (int numItemsPurchased, double dollars) {
        System.out.println("========================");
        System.out.println("YOUR PURCHASE");
        System.out.println("Items: " + numItemsPurchased + " Total: " + dollars);
        System.out.println("========================\n");
    }
}
