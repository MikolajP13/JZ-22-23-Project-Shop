package pl.edu.wszib.mpudelko.shop.product;

public final class Motherboard extends Product{
    private String chipsetType;

    public Motherboard() {
    }

    public Motherboard(String barcode, String productName, String brand, double unitPrice, int quantity, String chipsetType) {
        super(barcode, productName, brand, unitPrice, quantity);
        this.chipsetType = chipsetType + "''";
    }

    @Override
    public String toString() {
        return super.toString() + this.chipsetType;
    }
}
