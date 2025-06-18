package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    private final BigDecimal excise;

    protected Product(String name, BigDecimal price, BigDecimal tax, BigDecimal excise) {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        };
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        };
        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        };
        if (price.toString().charAt(0) == '-') {
            throw new IllegalArgumentException("Product price cannot be negative");
        };
        this.name = name;
        this.price = price.add(excise);
        this.taxPercent = tax;
        this.excise = excise;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getExcise() {
        return excise;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price);
    }
}