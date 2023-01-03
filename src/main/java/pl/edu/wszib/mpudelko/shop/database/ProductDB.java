package pl.edu.wszib.mpudelko.shop.database;

import pl.edu.wszib.mpudelko.shop.gui.GUI;
import pl.edu.wszib.mpudelko.shop.product.GraphicsCard;
import pl.edu.wszib.mpudelko.shop.product.Motherboard;
import pl.edu.wszib.mpudelko.shop.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDB {
    private final List<Product> products = new ArrayList<>();
    private final static ProductDB productDB = new ProductDB();
    private ProductDB() {
        products.add(new GraphicsCard("#gc001", "GeForce RTX 4090",
                "NVIDIA", 1599.99, 30, "24GB"));
        products.add(new GraphicsCard("#gc002", "Radeon 6950 XT",
                "AMD", 1299.00, 15, "16GB"));
        products.add(new GraphicsCard("#gc003", "GeForce RTX 3090 Ti",
                "NVIDIA", 1999.99, 20, "24GB"));
        products.add(new GraphicsCard("#gc004", "GeForce RTX 2080 Ti",
                "NVIDIA", 959.99, 3, "11GB"));
        products.add(new GraphicsCard("#gc005", "Radeon 6900 XT",
                "AMD", 998.59, 12, "16GB"));
        products.add(new GraphicsCard("#gc006", "GeForce RTX 2080",
                "NVIDIA", 699.00, 75, "8GB"));
        products.add(new GraphicsCard("#gc007", "GeForce GTX 1050 Ti",
                "MSI", 700.99, 42, "4GB"));
        products.add(new GraphicsCard("#gc008", "GeForce GTX 1660",
                "MSI", 450.99, 15, "6GB"));
        products.add(new Motherboard("#mb001", "AM4 TUF",
                "ASUS", 209.99, 48, "AMD 570X"));
        products.add(new Motherboard("#mb002", "B550F",
                "MSI", 204.19, 17, "AMD B550"));
        products.add(new Motherboard("#mb003", "MAXIMUS Z690",
                "ASUS", 649.00, 20, "Intel Z690"));

    }
    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> findByBarcode(String barcode){
        return this.products.stream()
                        .filter(p -> p.getBarcode().equals(barcode))
                        .findFirst();
    }

    public void addQuantityToProduct(Optional<GUI.Pair> pair){
        pair.ifPresent(p -> p.getProduct().setQuantity(p.getProduct().getQuantity() + p.getQuantity()));
    }

    public boolean buyProduct(String barcode, int quantity) {
        Optional<Product> productToBuy = findByBarcode(barcode);

        if (productToBuy.isPresent()
                && quantity > 0 && productToBuy.get().getQuantity() >= quantity) {
            productToBuy.get().setQuantity(productToBuy.get().getQuantity() - quantity);
            return true;
        }
        return false;
    }
    public static ProductDB getInstance(){
        return productDB;
    }
}
