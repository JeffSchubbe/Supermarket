import java.util.ArrayList;

// The pricing scheme for a single item type available at a supermarket.
public class ItemScheme {
    private String ID;                  // unique four-digit code to identify an item at the supermarket
    private double basePrice;           // starting price for an item (excludes taxes and discounts)
    private PricingCategory category;   // how the price of an item will be calculated
    private boolean chargeSalesTax;     // whether or not sales tax will be applied to this ite

    private int numToBuy;               // number of items that need to be purchased for the "BuyXGetY" deal to apply
    private int numFree;                // number of items that will not be charged when a "BuyXGetY" deal is applied
    private double extraTax;            // tax rate to be applied to the item on top of sales tax
    private ArrayList<Bundle> bundles;  // list of bundles that will be applied to related purchases

    public ItemScheme(String ID, double basePrice, PricingCategory category, boolean chargeSalesTax) {
        this.ID = ID;
        this.basePrice = basePrice;
        this.category = category;
        this.chargeSalesTax = chargeSalesTax;
        this.bundles = new ArrayList<Bundle>();
    }

    public String getID() {
        return ID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public PricingCategory getCategory() {
        return category;
    }

    public boolean isSalesTaxCharged() {
        return chargeSalesTax;
    }

    public int getNumFree() {
        return numFree;
    }

    public int getNumToBuy() {
        return numToBuy;
    }

    public void setBuyXGetYDeal(int numToBuy, int numFree) {
        this.numToBuy = numToBuy;
        this.numFree = numFree;
    }

    public double getExtraTax() {
        return extraTax;
    }

    public void setExtraTax(double extraTax) {
        this.extraTax = extraTax;
    }

    public ArrayList<Bundle> getBundles() {
        return bundles;
    }

    public void addBundle(Bundle bundle) {
        bundles.add(bundle);
    }
}
