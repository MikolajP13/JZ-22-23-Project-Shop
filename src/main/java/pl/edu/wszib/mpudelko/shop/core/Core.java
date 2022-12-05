package pl.edu.wszib.mpudelko.shop.core;

import pl.edu.wszib.mpudelko.shop.database.ProductDB;
import pl.edu.wszib.mpudelko.shop.database.UserDB;
import pl.edu.wszib.mpudelko.shop.gui.GUI;
import pl.edu.wszib.mpudelko.shop.user.User;

public class Core {
    private final GUI gui = GUI.getInstance();
    private final UserDB userDB = UserDB.getInstance();
    private final ProductDB productDB = ProductDB.getInstance();
    private final Authenticator authenticator = Authenticator.getInstance();
    private static final Core core = new Core();

    private Core() {
    }

    enum DisplayStatus {
        START_PAGE,
        MENU_PAGE
    }

    private DisplayStatus displayStartPage(){
        switch (this.gui.startPage()) {
            case "1" -> {
                if(signIn())
                    return DisplayStatus.MENU_PAGE;
            }
            case "2" -> {
                boolean result = signUp();
                this.gui.showSignUpResult(result);
                if (result && signIn())
                    return DisplayStatus.MENU_PAGE;
            }
            case "3" -> System.exit(0);
            default -> System.out.println("Option not supported! Sign in or sign up to get an access.");
        }
        return DisplayStatus.START_PAGE;
    }

    private DisplayStatus displayMenuPage(){
        boolean isAdmin = this.authenticator.getLoggedUser() != null
                && this.authenticator.getLoggedUser().getRole() == User.UserRole.ADMIN;

        switch (this.gui.showMenu()) {
            case "1" -> {
                if (isAdmin) {
                    if (this.gui.readSortInput()) {
                        this.gui.showSortedProducts();
                    } else {
                        this.gui.showProducts();
                    }
                } else {
                    this.gui.showProducts();
                }
            }
            case "2" -> this.gui.readProductDataAndShowBuyResult();
            case "3" -> {
                return signOut();
            }
            case "4" -> {
                if (isAdmin)
                    this.productDB.addQuantityToProduct(this.gui.readProductForAddQuantity());
            }
            case "5" -> {
                if (isAdmin)
                    this.gui.showUsers();
            }
            case "6" -> {
                if (isAdmin)
                    this.userDB.changeUserRole(this.gui.readUserLoginForRoleChange());
            }
            default -> System.out.println("Option not supported!");
        }
        return DisplayStatus.MENU_PAGE;
    }

    public void start() {
        boolean isRunning = true;
        DisplayStatus status = DisplayStatus.START_PAGE;

        while(isRunning){
            if(status == DisplayStatus.START_PAGE){
                status = displayStartPage();
            }
            if(status == DisplayStatus.MENU_PAGE){
               status = displayMenuPage();
            }
        }
    }

    private boolean signIn() {
        int counter = 3;
        while (counter != 0) {
            this.authenticator.authenticate(this.gui.readLoginAndPassword());
            if (authenticator.getLoggedUser() != null) {
                System.out.println("Welcome " + authenticator.getLoggedUser().getLogin() + "!");
                return true;
            }
            System.out.println("Incorrect login or password! " + (counter - 1) + " tries left.");
            counter--;
        }
        return false;
    }

    private boolean signUp() {
        User newUser = this.gui.readDataForNewUser();

        if (this.userDB.findByLogin(newUser.getLogin()).isEmpty()) {
            this.userDB.addUserToDatabase(newUser);
            return true;
        } else {
            return false;
        }
    }

    private DisplayStatus signOut() {
        this.authenticator.setLoggedUser(null);
        return DisplayStatus.START_PAGE;
    }

    public static Core getInstance() {
        return core;
    }
}