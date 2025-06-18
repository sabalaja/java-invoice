package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.BottleOfWine;

import java.math.BigDecimal;

public class Main {
    private static final int QUANTITY_TWO = 2;
    private static final int QUANTITY_THREE = 3;
    private static final int QUANTITY_FOUR = 4;
    private static final int QUANTITY_FIVE = 5;

    public static void main(String[] args) {
        Invoice invoice = new Invoice();

        Product candyBar = new TaxFreeProduct("Baton", new BigDecimal("1.04"));
        invoice.addProduct(candyBar, QUANTITY_THREE);

        Product waterBottle = new TaxFreeProduct("Woda", new BigDecimal("1.99"));
        invoice.addProduct(waterBottle);
        invoice.addProduct(waterBottle);

        Product bread = new DairyProduct("Chleb", new BigDecimal("3.54"));
        invoice.addProduct(bread, QUANTITY_TWO);

        Product soap = new OtherProduct("Kamen", new BigDecimal("3.50"));
        invoice.addProduct(soap, QUANTITY_FIVE);

        Product wine = new BottleOfWine("Wine", new BigDecimal("4.99"));
        invoice.addProduct(wine, QUANTITY_FOUR);

        Printer invoicePrinter = new PrintInvoice(invoice);
        invoicePrinter.print();
    }
}
