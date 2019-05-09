import java.util.ArrayList;
import java.util.HashMap;

// The object responsible for tracking the types of items a customer is purchasing and for calculating their total cost.
public class Checkout {
    private PricingScheme pricingScheme;            // the overall pricing scheme for a supermarket
    private double totalCost;                       // the total cost of a customer's shopping cart
    private double totalTax; // TODO: Track the total tax charged separate from the base charges.
    private int itemsPaidFor;                       // the total number of items in the customer's shopping cart
    private HashMap<String, Integer> itemsScanned;  // a map of each item type to the number that a customer had scanned at checkout

    public Checkout (PricingScheme pricingScheme) {
        this.pricingScheme = pricingScheme;
        this.totalCost = 0;
        this.totalTax = 0;
        itemsScanned = new HashMap<String, Integer>();
    }

    // Adds a single item from the customer to those that s/he is purchasing,
    // and the total cost of all items being purchased is updated.
    public void scan(String itemID) {
        if (pricingScheme.getItemSchemes().get(itemID) != null) { // item is not processed if its price does not exist
            //System.out.println("New item scanned: " + itemID);
            int itemCount = 0;
            if (itemsScanned.containsKey(itemID)) itemCount = itemsScanned.get(itemID);
            itemCount += 1;
            itemsScanned.put(itemID, itemCount);

            calculateNewTotal();

        } else {
            System.out.println("Item price not found.");
        }
    }

    // Calculates the new total of a customer's shopping cart.
    // All items that have been scanned will be totaled.
    // Each type of item may be calculated differently based on its PricingCategory.
    public void calculateNewTotal() {
        totalCost = 0;
        itemsPaidFor = 0;

        HashMap itemsScannedTemp = new HashMap<String, Integer>(itemsScanned); // used to track which items still need to be paid for
        ItemScheme scheme;
        double itemCost;
        double totalTaxRate;
        int numToPayFor;

        int numToBuy;
        int numFree;
        boolean bundleApplied;

        for (Object itemID : itemsScannedTemp.keySet()) {
            scheme = (ItemScheme) pricingScheme.getItemSchemes().get(itemID);
            itemCost = scheme.getBasePrice();
            totalTaxRate = 0;
            // Add regular sales tax per item if it applies for that item.
            if (scheme.isSalesTaxCharged()) totalTaxRate = pricingScheme.getSalesTax();

            switch (scheme.getCategory()) {
                case EXTRA_TAX:
                    totalTaxRate += scheme.getExtraTax();
                case SIMPLE:
                    itemCost *= 1 + totalTaxRate;
                    numToPayFor = (int) itemsScannedTemp.get(itemID);
                    itemsPaidFor += numToPayFor;
                    totalCost += numToPayFor * itemCost;
                    break;
                case BUY_X_GET_Y:
                    itemCost *= 1 + totalTaxRate;
                    numToBuy = scheme.getNumToBuy();
                    numFree = scheme.getNumFree();
                    numToPayFor = (int) itemsScannedTemp.get(itemID);
                    // Only charge for items that are not free.
                    while (numToPayFor > 0) {
                        if (numToPayFor > numToBuy) {
                            itemsPaidFor += numToBuy + numFree;
                            totalCost += numToBuy * itemCost;
                            numToPayFor -= numToBuy + numFree;
                            // Adjusts the itemsPaidFor count for scenarios where a
                            // customer does not "fully capitalize" on their deal.
                            if (numToPayFor < 0) itemsPaidFor += numToPayFor;
                        } else {
                            itemsPaidFor += numToPayFor;
                            totalCost += numToPayFor * itemCost;
                            numToPayFor = 0;
                        }
                    }
                    break;
                case BUNDLE:
                    // While purchased items still qualify for a bundle, add the cost of the bundle & remove contents of bundle from unpaid items.
                    ArrayList<Bundle> bundles = scheme.getBundles();
                    // TODO: Add prioritization for which bundle in the list is used first.
                    // (e.g. giving customer the bundle that would make their total cost the lowest).
                    for (Bundle bundle : bundles) {
                        // Only apply the bundle if at least 1 of each item in the bundle was scanned AND has not yet been paid for.
                        String[] bundleItems = bundle.getItemIDs();
                        bundleApplied = true;
                        for (String bundleItemID : bundleItems) {
                            if (!itemsScannedTemp.containsKey(bundleItemID) || ((int) itemsScannedTemp.get(bundleItemID) <= 0)) {
                                bundleApplied = false;
                            }
                        }
                        // The bundle deal is applied while there is still at least 1 of each item in the bundle left to be paid for.
                        while (bundleApplied) {
                            // Pay for 1 of each item in the bundle.
                            itemsPaidFor += bundleItems.length;
                            totalCost += bundle.getBundlePrice() * (1+totalTaxRate);
                            for (String bundleItemID : bundleItems) {
                                itemsScannedTemp.put(bundleItemID, (int) itemsScannedTemp.get(bundleItemID) - 1);
                                if ((int) itemsScannedTemp.get(bundleItemID) <= 0) bundleApplied = false;
                            }
                        }
                    }
                    // Add remaining items to price total.
                    numToPayFor = (int) itemsScannedTemp.get(itemID);
                    itemsPaidFor += numToPayFor;
                    totalCost += numToPayFor * itemCost * (1+totalTaxRate);
                    break;
            }
        }
        //System.out.println("====== ITEMS: " + itemsPaidFor + " TOTAL: $" + totalCost + " ======");
    }

    public double getTotal() {
        return totalCost;
    }

    public int getItemsPaidFor() {
        return itemsPaidFor;
    }
}
