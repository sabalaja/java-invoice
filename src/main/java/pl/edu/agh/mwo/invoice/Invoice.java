package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private static final int INVOICE_RANDOM_NUMBER_MAX = 9000;
    private static final int INVOICE_RANDOM_NUMBER_MIN = 1000;

    private Map<Product, Integer> products = new HashMap<Product, Integer>();
    private final String invoiceNumber;

    public Invoice() {
        Random rand = new Random();
        this.invoiceNumber = "INV" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"))
                + (rand.nextInt(INVOICE_RANDOM_NUMBER_MAX) + INVOICE_RANDOM_NUMBER_MIN);
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Set<Map.Entry<Product, Integer>> getProductsEntrySet() {
        return products.entrySet();
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero");
        };
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        };
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        };
        products.compute(product, (k, v) -> v == null ? quantity : v + quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity)
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity)
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return totalGross;
    }
}