package pl.edu.wszib.mpudelko.shop.core;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.mpudelko.shop.database.UserDB;
import pl.edu.wszib.mpudelko.shop.user.User;

import java.util.Optional;

public class Authenticator {
    private final String seed = "o3S&OiSPjeP#Pcvq^vOW!HxfoUkQt&0A6py!1vA9^pBpsRd#kl";
    private final UserDB userDB = UserDB.getInstance();
    private final static Authenticator authenticator = new Authenticator();
    private User loggedUser = null;

    private Authenticator() {
    }

    public void authenticate(User user) {
        Optional<User> userToAuthenticate = this.userDB.findByLogin(user.getLogin());

        if (userToAuthenticate.isPresent() &&
                userToAuthenticate.get().getPassword()
                        .equals(DigestUtils.md5Hex(user.getPassword() + this.getSeed()))) {
            this.loggedUser = userToAuthenticate.get();
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getSeed() {
        return seed;
    }

    public static Authenticator getInstance() {
        return authenticator;
    }
}
