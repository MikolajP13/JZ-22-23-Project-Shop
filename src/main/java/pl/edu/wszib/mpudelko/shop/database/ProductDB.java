package pl.edu.wszib.mpudelko.shop.database;

import pl.edu.wszib.mpudelko.shop.gui.GUI;
import pl.edu.wszib.mpudelko.shop.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDB {
    private final List<Product> products = new ArrayList<>();
    private final static ProductDB productDB = new ProductDB();
    private ProductDB() {
        products.add(new Product("#gc001", "GeForce RTX 4090",
                "NVIDIA", 1599.99, 30));
        products.add(new Product("#gc002", "Radeon 6950 XT",
                "AMD", 1299.00, 15));
        products.add(new Product("#gc003", "GeForce RTX 3090 Ti",
                "NVIDIA", 1999.99, 20));
        products.add(new Product("#gc004", "GeForce RTX 2080 Ti",
                "NVIDIA", 959.99, 3));
        products.add(new Product("#gc005", "Radeon 6900 XT",
                "AMD", 998.59, 12));
        products.add(new Product("#gc006", "GeForce RTX 2080",
                "NVIDIA", 699.00, 75));
        products.add(new Product("#gc007", "GeForce GTX 1050 Ti",
                "MSI", 700.99, 42));
        products.add(new Product("#gc008", "GeForce GTX 1660",
                "MSI", 450.99, 15));
    }
    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> findByBarcode(String barcode){
        for (Product product : this.products) {
            if (product.getBarcode().equals(barcode)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
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
