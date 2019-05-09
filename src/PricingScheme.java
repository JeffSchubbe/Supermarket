import java.util.HashMap;

// The pricing scheme for all items available at a supermarket.
public class PricingScheme {
    private HashMap<String,ItemScheme> itemSchemes; // all item schemes in the supermarket mapped to their unique IDs
    private double salesTax; // the sales tax rate to be applied to all applicable items in the supermarket

    public PricingScheme(HashMap<String, ItemScheme> itemSchemes, double salesTax) {
        this.itemSchemes = itemSchemes;
        this.salesTax = salesTax;
    }

    // Retrieves the hash map of the pricing schemes of every individual item at the supermarket.
    public HashMap getItemSchemes() {
        return itemSchemes;
    }

    // Retrieve the sales tax rate to be applied to all applicable items in the supermarket.
    public double getSalesTax() {
        return salesTax;
    }
}