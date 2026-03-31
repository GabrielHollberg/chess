package dataaccess;

import exception.DataAccessException;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

// Provides methods for AuthData memory access
public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() {}

    public void createUserData(UserData userData) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("INSERT INTO user_data (username, password, email) VALUES(?, ?, ?)");
                String username = userData.username();
                String password = userData.password();
                String email = userData.email();
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public UserData readUserData(String username) {
        return DatabaseManager.readUserData(username);
    }

    public void updateUserData(UserData userData) {
        //users.put(userData.username(), userData);
    }

    public void deleteUserData(String username) {
        //users.remove(username);
    }

    public void deleteAllUserData() {
        DatabaseManager.deleteAllUserData();
    }
}
