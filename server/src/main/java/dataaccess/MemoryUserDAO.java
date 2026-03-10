package dataaccess;

import model.UserData;

import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> users;

    public MemoryUserDAO(Map<String, UserData> users) {
        this.users = users;
    }

    public void createUserData(UserData userData) {
        users.put(userData.username(), userData);
    }

    public UserData readUserData(String username) {
        return users.get(username);
    }

    public void updateUserData(UserData userData) {
        users.put(userData.username(), userData);
    }

    public void deleteUserData(String username) {
        users.remove(username);
    }

    public void deleteAllUserData() {
        users.clear();
    }
}
