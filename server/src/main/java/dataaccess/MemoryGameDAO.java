package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Map;

// Provides methods for AuthData memory access
public class MemoryGameDAO implements GameDAO {

    private final Map<Integer,GameData> games;

    public MemoryGameDAO(Map<Integer, GameData> games) {
        this.games = games;
    }

    public int createGameData(GameData gameData) {
        games.put(gameData.gameID(), gameData);
        return gameData.gameID();
    }

    public GameData readGameData(int gameID) {
        return games.get(gameID);
    }

    public ArrayList<GameData> readAllGameData() {
        return null;
    }

    public void updateGameData(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    public void deleteAllGameData() {
        games.clear();
    }
}
