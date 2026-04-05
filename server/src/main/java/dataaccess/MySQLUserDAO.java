package dataaccess;

import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

// Provides methods for AuthData memory access
public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() {}

    public void createUserData(UserData userData) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("INSERT INTO user_data (username, password, email) VALUES(?, ?, ?)");
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public UserData readUserData(String username) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM user_data WHERE username=?");
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                String usernameTemp;
                String password;
                String email;
                if (rs.next()) {
                    usernameTemp = rs.getString("username");
                    password = rs.getString("password");
                    email = rs.getString("email");
                    return new UserData(usernameTemp, password, email);
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public void deleteAllUserData() {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("TRUNCATE user_data");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }
}
