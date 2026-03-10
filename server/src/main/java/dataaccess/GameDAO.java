package dataaccess;

import model.GameData;

public interface GameDAO {

    public void createGameData(GameData gameData);

    public GameData readGameData(int gameID);

    public void updateGameData(GameData gameData);

    public void deleteGameData(int gameID);

    public void deleteAllGameData();
}
