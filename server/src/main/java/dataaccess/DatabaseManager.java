package dataaccess;

import exception.DataAccessException;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var databaseStatement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        var userDataTableStatement = "CREATE TABLE IF NOT EXISTS userData (\n" +
                "username VARCHAR(255) NOT NULL,\n" +
                "password VARCHAR(255) NOT NULL,\n" +
                "email VARCHAR(255) NOT NULL\n" +
                ")";

        var gameDataTableStatement = "CREATE TABLE IF NOT EXISTS gameData (\n" +
                "game_id VARCHAR(255) NOT NULL,\n" +
                "white_username VARCHAR(255) NOT NULL,\n" +
                "black_username VARCHAR(255) NOT NULL,\n" +
                "game_name VARCHAR(255) NOT NULL\n" +
                ")";

        var authDataTableStatement = "CREATE TABLE IF NOT EXISTS authData (\n" +
                "auth_token VARCHAR(255) NOT NULL,\n" +
                "username VARCHAR(255) NOT NULL\n" +
                ")";

        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedDatabaseStatement = conn.prepareStatement(databaseStatement)) {
            preparedDatabaseStatement.executeUpdate();

            conn.setCatalog(databaseName);

            try (var preparedTableStatement = conn.prepareStatement(userDataTableStatement)) {
                preparedTableStatement.executeUpdate();
            }
            try (var preparedTableStatement = conn.prepareStatement(gameDataTableStatement)) {
                preparedTableStatement.executeUpdate();
            }
            try (var preparedTableStatement = conn.prepareStatement(authDataTableStatement)) {
                preparedTableStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    static int insertUserData(String username, String password, String email) throws SQLException {
        try {
            var conn = getConnection();
            try (var preparedStatement = conn.prepareStatement("INSERT INTO userData (username, password, email) VALUES(?, ?, ?)", RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                var ID = 0;
                if (resultSet.next()) {
                    ID = resultSet.getInt(1);
                }

                return ID;
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
