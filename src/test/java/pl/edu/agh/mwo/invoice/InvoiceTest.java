package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void setupInvoice() {
        invoice = new Invoice();
    }

    @Test
    public void invoiceNumberShouldBeTwentyTwoCharactersLong() {
        Assert.assertEquals(22, invoice.getInvoiceNumber().length());
    }

    @Test
    public void invoiceNumberRandomPartInExpectedRange() {
        String randomPart = invoice.getInvoiceNumber().substring(18);
        int random = Integer.parseInt(randomPart);
        Assert.assertTrue(random >= 1000 && random <= 9999);
    }

    @Test
    public void invoiceNumberShouldStartWithINV() {
        Assert.assertEquals("INV", invoice.getInvoiceNumber().substring(0, 3));
    }

    @Test
    public void invoiceDatePortionShouldHaveValidFormat() {
        int year = Integer.parseInt(invoice.getInvoiceNumber().substring(3, 5));
        int month = Integer.parseInt(invoice.getInvoiceNumber().substring(5, 7));
        int day = Integer.parseInt(invoice.getInvoiceNumber().substring(7, 9));
        int hour = Integer.parseInt(invoice.getInvoiceNumber().substring(9, 11));
        int minute = Integer.parseInt(invoice.getInvoiceNumber().substring(11, 13));
        int second = Integer.parseInt(invoice.getInvoiceNumber().substring(13, 15));
        int millisecond = Integer.parseInt(invoice.getInvoiceNumber().substring(15, 18));

        Assert.assertTrue(month >= 1 && month <= 12);
        Assert.assertTrue(day >= 1 && day <= 31);
        Assert.assertTrue(hour >= 0 && hour <= 23);
        Assert.assertTrue(minute >= 0 && minute <= 59);
        Assert.assertTrue(second >= 0 && second <= 59);
        Assert.assertTrue(millisecond >= 0 && millisecond <= 999);
    }

    @Test
    public void netTotalShouldBeZeroForNewInvoice() {
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void taxTotalShouldBeZeroInitially() {
        Assert.assertThat(invoice.getTaxTotal(), Matchers.comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void grossTotalShouldBeZeroAtStart() {
        Assert.assertThat(invoice.getGrossTotal(), Matchers.comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void addingOneTaxFreeProductIncreasesNetTotal() {
        Product bread = new TaxFreeProduct("Bread", new BigDecimal("4.50"));
        invoice.addProduct(bread);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(new BigDecimal("4.50")));
    }

    @Test
    public void taxIsCorrectForSingleOtherProduct() {
        Product toy = new OtherProduct("Toy", new BigDecimal("20.00"));
        invoice.addProduct(toy);
        Assert.assertThat(invoice.getTaxTotal(), Matchers.comparesEqualTo(new BigDecimal("4.60")));
    }

    @Test
    public void grossTotalCorrectForMultipleProductTypes() {
        invoice.addProduct(new TaxFreeProduct("Notebook", new BigDecimal("12.00")));
        invoice.addProduct(new DairyProduct("Cheese", new BigDecimal("20.00")));
        invoice.addProduct(new OtherProduct("Headphones", new BigDecimal("50.00")));

        BigDecimal expectedTotal = new BigDecimal("95.10");
        Assert.assertThat(invoice.getGrossTotal(), Matchers.comparesEqualTo(expectedTotal));
    }

    @Test
    public void netTotalUpdatedCorrectlyAfterAddingSameProductTwice() {
        Product juice = new DairyProduct("Juice", new BigDecimal("8.00"));
        invoice.addProduct(juice);
        invoice.addProduct(juice);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(new BigDecimal("16.00")));
    }

    @Test
    public void invoiceHandlesMultipleQuantitiesCorrectly() {
        Product pencil = new TaxFreeProduct("Pencil", new BigDecimal("1.00"));
        invoice.addProduct(pencil, 100);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(new BigDecimal("100.00")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invoiceRejectsNegativeQuantity() {
        Product yogurt = new DairyProduct("Yogurt", new BigDecimal("3.50"));
        invoice.addProduct(yogurt, -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invoiceRejectsNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void sameProductWithDifferentQuantitiesShouldBeSummed() {
        Product nails = new OtherProduct("Nails", new BigDecimal("0.50"));
        invoice.addProduct(nails, 40);
        invoice.addProduct(nails, 10);
        Assert.assertEquals(50, (long) invoice.getProducts().get(nails));
    }

    @Test
    public void exciseTaxShouldBeAddedToFuelCanister() {
        Product fuel = new FuelCanister("Fuel", new BigDecimal("20.00"));
        Assert.assertThat(fuel.getExcise(), Matchers.comparesEqualTo(new BigDecimal("5.56")));
    }

    @Test
    public void priceWithExciseShouldBeCorrect() {
        Product wine = new BottleOfWine("Wine", new BigDecimal("25.00"));
        Assert.assertThat(wine.getPrice(), Matchers.comparesEqualTo(new BigDecimal("30.56")));
    }

    @Test
    public void productTaxShouldIncludeExciseForBottleOfWine() {
        Product wine = new BottleOfWine("Wine", new BigDecimal("10.00"));
        Assert.assertThat(wine.getPriceWithTax(), Matchers.comparesEqualTo(new BigDecimal("19.1388")));
    }

    @Test
    public void invoiceCalculatesCorrectGrossTotalForMultipleQuantities() {
        invoice.addProduct(new DairyProduct("Cream", new BigDecimal("4.00")), 5);
        invoice.addProduct(new OtherProduct("Candy", new BigDecimal("2.00")), 10);
        invoice.addProduct(new BottleOfWine("Merlot", new BigDecimal("6.00")), 2);

        BigDecimal expectedGross = new BigDecimal("74.64");
        Assert.assertThat(invoice.getGrossTotal(), Matchers.comparesEqualTo(expectedGross));
    }

    @Test
    public void addingTaxFreeAndTaxedProductsGivesCorrectNetTotal() {
        invoice.addProduct(new TaxFreeProduct("Apple", new BigDecimal("1.00")), 5);
        invoice.addProduct(new OtherProduct("USB Cable", new BigDecimal("10.00")), 2);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(new BigDecimal("25.00")));
    }
}
