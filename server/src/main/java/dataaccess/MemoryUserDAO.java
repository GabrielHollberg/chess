package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData readUser(String username) {
        return users.get(username);
    }

    @Override
    public void updateUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username);
    }
}
