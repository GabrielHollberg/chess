package dataaccess;

import exception.DataAccessException;
import model.GameData;
import model.LightGameData;

import java.sql.SQLException;
import java.util.Map;

// Provides methods for AuthData memory access
public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() {}

    public void createGameData(GameData gameData) {
        try {
            DatabaseManager.insertGameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData readGameData(int gameID) {
        return DatabaseManager.readGameData(gameID);
    }

    /*public GameData readGameData(int gameID) {
        return DatabaseManager.readGameData(gameID);
    }*/

    public Map<Integer,GameData> readAllGameData() {
        //return games;
        return null;
    }

    public void updateGameData(GameData gameData) {
        //games.put(gameData.gameID(), gameData);
    }

    public void deleteGameData(int gameID) {
        //games.remove(gameID);
    }

    public void deleteAllGameData() {
        DatabaseManager.deleteAllGameData();
    }
}
