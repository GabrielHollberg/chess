package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private HashMap<Integer,GameData> games;

    public void createGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
    }

    public GameData readGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        games.remove(gameData.gameID());
        games.put(gameData.gameID(), gameData);
    }

    public void deleteGame(int gameID) throws DataAccessException {
        games.remove(gameID);
    }
}
