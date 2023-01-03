package pl.edu.wszib.mpudelko.shop.database;

import pl.edu.wszib.mpudelko.shop.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDB {
    private final List<User> users = new ArrayList<>();
    private final static UserDB userDB = new UserDB();
    private UserDB() {
        users.add(new User("admin", "be17c4239e78ffea11ec041bfc7de5cd",
                User.UserRole.ADMIN)); // log: admin pass: admin
        users.add(new User("testowy12", "a29df47be118e35f44acc9094b32596b",
                User.UserRole.USER)); // log: testowy12 pass: test
    }

    public void addUserToDatabase(User newUser){
        this.users.add(newUser);
    }

    public Optional<User> findByLogin(String userLogin){
        return this.users.stream()
                .filter(u->u.getLogin().equals(userLogin))
                .findFirst();
    }

    public void changeUserRole(Optional<User> userBox){
        userBox.ifPresent(user ->
                user.setRole(user.getRole() == User.UserRole.USER
                        ? User.UserRole.ADMIN : User.UserRole.USER));
    }

    public List<User> getUsers() {
        return users;
    }

    public static UserDB getInstance(){
        return userDB;
    }
}
