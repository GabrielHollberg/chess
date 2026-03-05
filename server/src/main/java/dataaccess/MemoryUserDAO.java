package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    public HashMap<String,UserData> users;

    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public UserData readUser(String username) {
        return users.get(username);
    }

    public void updateUser(UserData userData) {
        users.remove(userData.username());
        users.put(userData.username(), userData);
    }

    public void deleteUser(String username) {
        users.remove(username);
    }
}
