package pl.edu.wszib.mpudelko.shop.user;

public class User {
    private String login;
    private String password;
    private UserRole role;
    public User() {
    }

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("%15s, %32s, %8s", this.getLogin(), this.getPassword(), this.getRole());
    }

    public enum UserRole {
        ADMIN,
        USER
    }
}
