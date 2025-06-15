package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Map;
import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<Product, Integer>();
    private int InvoiceNumber;
    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        this.products.put(product, 1);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getSubtotal() {
        return getTotal().subtract(getTax());
    }

    public BigDecimal getTax() {
        BigDecimal totalTax = new BigDecimal(0);
        Iterator<Map.Entry<Product, Integer>> it = products.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Product, Integer> pair = it.next();
            BigDecimal productTax = pair.getKey().getTaxOnly();
            BigDecimal quantity = new BigDecimal(pair.getValue());
            totalTax = totalTax.add(productTax.multiply(quantity));
        }
        return totalTax;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        Iterator<Map.Entry<Product, Integer>> it = products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Product, Integer> pair = it.next();
            BigDecimal productPriceWithTax = pair.getKey().getPriceWithTax();
            BigDecimal quantity = new BigDecimal(pair.getValue());
            total = total.add(productPriceWithTax.multiply(quantity));
        }
        return total;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

}
}
