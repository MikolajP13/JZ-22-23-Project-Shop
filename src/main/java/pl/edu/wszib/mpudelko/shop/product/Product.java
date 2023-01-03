package pl.edu.wszib.mpudelko.shop.product;

public sealed class Product permits GraphicsCard, Motherboard {
    private String barcode;
    private String productName;
    private String brand;
    private double unitPrice;
    private int quantity;

    public Product() {
    }

    public Product(String barcode, String productName, String brand, double unitPrice, int quantity) {
        this.barcode = barcode;
        this.productName = productName;
        this.brand = brand;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%-8s%-25s%-15s%-10.2f%-10d", this.barcode,
                this.productName, this.brand, this.unitPrice, this.quantity);
    }
}

