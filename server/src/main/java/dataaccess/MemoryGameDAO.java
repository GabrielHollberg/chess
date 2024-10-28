package dataaccess;

import Model.GameData;
import Model.UserData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> gameDataList;

    public void clear() {
        gameDataList.clear();
    }

    public void addGameData(int gameID, GameData gameData) {
        gameDataList.put(gameID, gameData);
    }

    public void deleteGameData(int gameID) {
        gameDataList.remove(gameID);
    }

    public GameData getGameData(int gameID) {
        return gameDataList.get(gameID);
    }
}
