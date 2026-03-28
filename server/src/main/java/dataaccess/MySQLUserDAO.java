package dataaccess;

import exception.DataAccessException;
import model.UserData;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

// Provides methods for AuthData memory access
public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO(Map<String, UserData> users) {}

    public void createUserData(UserData userData) {
        try {
            DatabaseManager.insertUserData(userData.username(), userData.password(), userData.email());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserData readUserData(String username) {
        //return users.get(username);
        return null;
    }

    public void updateUserData(UserData userData) {
        //users.put(userData.username(), userData);
    }

    public void deleteUserData(String username) {
        //users.remove(username);
    }

    public void deleteAllUserData() {
        //users.clear();
    }
}
