// Defines a Bundle object to be used to calculate items
// being sold at a discount when 1 of each them are purchased together.
public class Bundle {
    private String[] itemIDs;   // all items that must be purchased for the bundle price to be applied
    private double bundlePrice; // the total price to be charged for 1 of each item in the bundle

    public Bundle(String[] IDs, double price) {
        itemIDs = IDs;
        bundlePrice = price;
    }

    public String[] getItemIDs() {
        return itemIDs;
    }

    public double getBundlePrice() {
        return bundlePrice;
    }
}
