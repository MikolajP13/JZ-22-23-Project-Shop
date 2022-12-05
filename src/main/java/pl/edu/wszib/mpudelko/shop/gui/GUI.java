package pl.edu.wszib.mpudelko.shop.gui;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.mpudelko.shop.core.Authenticator;
import pl.edu.wszib.mpudelko.shop.core.LoginValidator;
import pl.edu.wszib.mpudelko.shop.core.PasswordValidator;
import pl.edu.wszib.mpudelko.shop.database.ProductDB;
import pl.edu.wszib.mpudelko.shop.database.UserDB;
import pl.edu.wszib.mpudelko.shop.product.Product;
import pl.edu.wszib.mpudelko.shop.user.User;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class GUI {
    private final static Scanner scanner = new Scanner(System.in);
    private final static GUI gui = new GUI();
    private final ProductDB productDB = ProductDB.getInstance();
    private final UserDB userDB = UserDB.getInstance();
    private final Authenticator authenticator = Authenticator.getInstance();
    private final LoginValidator loginValidator = LoginValidator.getInstance();
    private final PasswordValidator passwordValidator = PasswordValidator.getInstance();
    private final String productListLabel = String.format("%-8s%-25s%-15s%-10s%-10s",
            "Barcode", "Product", "Brand", "Price $", "Quantity");

    private GUI() {
    }

    public String startPage() {
        System.out.println("1. Sign in");
        System.out.println("2. Sign up");
        System.out.println("3. Quit");

        return scanner.nextLine();
    }

    public String showMenu() {
        System.out.println("1. List products");
        System.out.println("2. Buy products");
        System.out.println("3. Sign out");
        if (this.authenticator.getLoggedUser() != null
                && this.authenticator.getLoggedUser().getRole() == User.UserRole.ADMIN) {
            System.out.println("4. Add quantity to product");
            System.out.println("5. List users");
            System.out.println("6. Change user role");
        }
        return scanner.nextLine();
    }

    public User readLoginAndPassword() {
        User user = new User();
        System.out.print("Login: ");
        user.setLogin(scanner.nextLine());
        System.out.print("Password: ");
        user.setPassword(scanner.nextLine());
        return user;
    }

    public void showProducts() {
        System.out.println(this.productListLabel);
        this.productDB.getProducts().forEach(System.out::println);
    }

    public boolean readSortInput() {
        System.out.print("Sort products by quantity (y/n)? ");
        return scanner.nextLine().equalsIgnoreCase("y");
    }

    public void showSortedProducts() {
        System.out.println(this.productListLabel);
        this.productDB.getProducts().stream()
                .sorted((Comparator.comparingInt(Product::getQuantity)))
                .forEach(System.out::println);
    }

    public void showSignUpResult(boolean result) {
        if (result)
            System.out.println("Registration was successful. Now log in to your account.");
        else
            System.out.println("User with this login already exists!");
    }

    public Optional<Pair> readProductForAddQuantity() {
        Pair pair = new Pair();
        System.out.print("Enter product barcode: ");
        String productBarcode = scanner.nextLine();
        Optional<Product> product = this.productDB.findByBarcode(productBarcode);
        int quantity = -1;

        if (product.isPresent()) {
            pair.setProduct(product.get());

            System.out.print("Enter quantity: ");
            if (scanner.hasNextInt())
                quantity = scanner.nextInt();

            scanner.nextLine();

            if (quantity <= 0) {
                System.out.println("Incorrect value of quantity!");
                return Optional.empty();
            }

            pair.setQuantity(quantity);

            System.out.println("Added " + quantity + (quantity == 1 ? " item " : " items ")
                    + "to " + product.get().getProductName());

            return Optional.of(pair);

        } else {
            System.out.println("Product with barcode " + productBarcode + " doesn't exists!");
            return Optional.empty();
        }
    }

    public static class Pair {
        private Product product;
        private int quantity;

        public Pair() {
        }

        public Pair(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public void showUsers() {
        System.out.printf("%-17s%-34s%-8s\n", "Login", "Password", "Role");
        this.userDB.getUsers()
                .forEach(user ->
                        System.out.printf("%-17s%-34s%-8s\n",
                                user.getLogin(), user.getPassword(), user.getRole()));
    }

    public Optional<User> readUserLoginForRoleChange() {
        System.out.print("Enter user login: ");
        String userLogin = scanner.nextLine();
        Optional<User> user = this.userDB.findByLogin(userLogin);

        if (user.isPresent() && this.authenticator.getLoggedUser() != user.get()
                && this.authenticator.getLoggedUser() != user.get()) {
            System.out.println("User role has been changed!");
            return user;
        } else {
            System.out.println("You can't change your role or user doesn't exists in database!");
            return Optional.empty();
        }
    }

    public void readProductDataAndShowBuyResult() {
        int quantity = -1;
        System.out.print("Enter product barcode: ");
        String barcode = scanner.nextLine();
        System.out.print("Enter quantity: ");
        if (scanner.hasNextLong())
            quantity = scanner.nextInt();
        scanner.nextLine();
        if (this.productDB.buyProduct(barcode, quantity)) {
            System.out.println("You've just bought "
                    + quantity + (quantity == 1 ? " item" : " items")
                    + " with barcode " + barcode);
        } else {
            System.out.println("Product with barcode " + barcode
                    + " doesn't exists or input value is incorrect!");
        }
    }

    public User readDataForNewUser() {
        User newUser = new User();
        System.out.print("Enter you login: ");
        String login = scanner.nextLine();
        while (!this.loginValidator.isValid(login) || this.userDB.findByLogin(login).isPresent()) {
            System.out.println("User name exists or login is incorrect!" +
                    "\n(info: login must be 6-15 long, no special characters and cannot start with a digit)");
            System.out.print("Enter you login: ");
            login = scanner.nextLine();
        }
        newUser.setLogin(login);

        System.out.print("Enter you password: ");
        String password = scanner.nextLine();
        while(!this.passwordValidator.isValid(password)) {
            System.out.println("(info: password must contain a length of at least 8 characters " +
                    "and at least: one special character, one digit, lower and uppercase character)");
            System.out.print("Enter you password: ");
            password = scanner.nextLine();
        }
        newUser.setPassword(DigestUtils.md5Hex(password + authenticator.getSeed()));
        newUser.setRole(User.UserRole.USER);
        return newUser;
    }

    public static GUI getInstance() {
        return gui;
    }
}
