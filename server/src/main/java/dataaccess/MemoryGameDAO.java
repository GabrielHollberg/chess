package dataaccess;

import model.GameData;

import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> games;

    public MemoryGameDAO(Map<Integer, GameData> games) {
        this.games = games;
    }

    public void createGameData(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    public GameData readGameData(int gameID) {
        return games.get(gameID);
    }

    public void updateGameData(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    public void deleteGameData(int gameID) {
        games.remove(gameID);
    }

    public void deleteAllGameData() {
        games.clear();
    }
}
