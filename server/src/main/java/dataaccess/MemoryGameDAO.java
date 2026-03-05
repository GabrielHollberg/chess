package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO {

    public HashMap<Integer,GameData> games;

    void createGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
    }

    GameData readGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    void updateGame(GameData gameData) throws DataAccessException {
        games.remove(gameData.gameID());
        games.put(gameData.gameID(), gameData);
    }

    void deleteGame(int gameID) throws DataAccessException {
        games.remove(gameID);
    }
}
