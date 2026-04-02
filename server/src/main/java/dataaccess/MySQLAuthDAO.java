package dataaccess;

import exception.DataAccessException;
import model.AuthData;

import java.sql.SQLException;
import java.util.Map;

// Provides methods for AuthData memory access
public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO() {}

    public void createAuthData(AuthData authData) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("INSERT INTO auth_data (auth_token, username) VALUES(?, ?)");
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());

                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public AuthData readAuthData(String authToken) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("SELECT username FROM auth_data WHERE auth_token=?");
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return new AuthData(authToken, rs.getString("username"));
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public void updateAuthData(AuthData authData) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("UPDATE auth_data SET username=? WHERE auth_token=?");
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public void deleteAuthData(String authToken) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("DELETE FROM auth_data WHERE auth_token=?");
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public void deleteAllAuthData() {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("TRUNCATE auth_data");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }
}
