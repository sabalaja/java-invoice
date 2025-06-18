package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

public class PrintInvoice implements Printer {
    private static final int MAX_NAME_LENGTH = 26;
    private static final int SPACING_NAME_TO_QTY = 37;
    private static final int SPACING_QTY_TO_PRICE = 20;

    private static final int SPACING_TOTAL_TYPES = 10;
    private static final int SPACING_TOTAL_QTY = 15;
    private static final int SPACING_TOTAL_PRICE = 18;

    private final Invoice invoice;

    public PrintInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void print() {
        Set<Map.Entry<Product, Integer>> entries = invoice.getProductsEntrySet();
        int totalQuantity = 0;

        StringBuilder output = new StringBuilder();
        output.append("Invoice Number: ").append(invoice.getInvoiceNumber()).append("\n");

        for (Map.Entry<Product, Integer> entry : entries) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalQuantity += quantity;

            String name = product.getName();
            if (name.length() > MAX_NAME_LENGTH) {
                name = name.substring(0, MAX_NAME_LENGTH);
            }

            String quantityStr = String.valueOf(quantity);
            BigDecimal totalPrice = product.getPriceWithTax().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
            String priceStr = totalPrice.toString();

            int spacingNameQty = Math.max(1, SPACING_NAME_TO_QTY - name.length() - quantityStr.length());
            int spacingQtyPrice = Math.max(1, SPACING_QTY_TO_PRICE - priceStr.length());

            output.append(name).append(" ".repeat(spacingNameQty)).append(quantityStr).append(" ".repeat(spacingQtyPrice)).append(priceStr).append("\n");
        }

        String totalPriceStr = invoice.getGrossTotal().toString();

        output.append("\n").append("Total product types:").append(" ".repeat(Math.max(1, SPACING_TOTAL_TYPES - String.valueOf(entries.size()).length()))).append(entries.size()).append("\n");

        output.append("Total quantity:").append(" ".repeat(Math.max(1, SPACING_TOTAL_QTY - String.valueOf(totalQuantity).length()))).append(totalQuantity).append("\n");

        output.append("Total price:").append(" ".repeat(Math.max(1, SPACING_TOTAL_PRICE - totalPriceStr.length()))).append(totalPriceStr).append("\n");

        System.out.println(output);
    }
}
