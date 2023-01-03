package pl.edu.wszib.mpudelko.shop.product;

public final class GraphicsCard extends Product{
    private String graphicsRamSize;

    public GraphicsCard() {
    }

    public GraphicsCard(String barcode, String productName, String brand, double unitPrice, int quantity, String graphicsRamSize) {
        super(barcode, productName, brand, unitPrice, quantity);
        this.graphicsRamSize = graphicsRamSize + "'";
    }

    @Override
    public String toString() {
        return super.toString() + this.graphicsRamSize;
    }
}
