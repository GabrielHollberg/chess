package dataaccess;

import model.GameData;

import java.util.Map;

public interface GameDAO {

    public void createGameData(GameData gameData);

    public GameData readGameData(int gameID);

    public Map<Integer,GameData> readAllGameData();

    public void updateGameData(GameData gameData);

    public void deleteGameData(int gameID);

    public void deleteAllGameData();
}
