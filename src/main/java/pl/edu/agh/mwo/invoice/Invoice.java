package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private HashMap<Product, Integer> products = new HashMap<Product, Integer>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        } else {
            products.put(product, 1);
        }
    }

    public void addProduct(Product product, Integer quantity) {
        if (quantity<=0) {
            throw new IllegalArgumentException("Quantity cannot be less than 0");
        } else {
            products.put(product, quantity);
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal subTotal = BigDecimal.ZERO;
        if (this.products != null) {
            for (Product product : products.keySet()) {
                subTotal = subTotal.add(product.getPrice().multiply(BigDecimal.valueOf(products.get(product))));
            }
        }
        return subTotal;
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;
        if (this.products != null) {
            for (Product product : products.keySet()) {
                tax = tax.add(product.getPrice().multiply(product.getTaxPercent().multiply(BigDecimal.valueOf(products.get(product)))));
            }
        }
        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (this.products != null) {
            System.out.println(getSubtotal());
            total = getSubtotal().add(getTax());
        }
        return total;
    }
}
