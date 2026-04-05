package dataaccess;

import exception.DataAccessException;

import java.sql.*;
import java.util.Properties;

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

        var userDataTableStatement = "CREATE TABLE IF NOT EXISTS user_data (\n" +
                "username VARCHAR(255) NOT NULL,\n" +
                "password VARCHAR(255) NOT NULL,\n" +
                "email VARCHAR(255) NOT NULL\n" +
                ")";

        var gameDataTableStatement = "CREATE TABLE IF NOT EXISTS game_data (\n" +
                "game_id INT NOT NULL AUTO_INCREMENT,\n" +
                "white_username VARCHAR(255),\n" +
                "black_username VARCHAR(255),\n" +
                "game_name VARCHAR(255) NOT NULL,\n" +
                "game TEXT NOT NULL,\n" +
                "PRIMARY KEY (game_id)" +
                ")";

        var authDataTableStatement = "CREATE TABLE IF NOT EXISTS auth_data (\n" +
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
