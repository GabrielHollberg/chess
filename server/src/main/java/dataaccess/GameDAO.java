package dataaccess;

import model.GameData;

import java.util.ArrayList;

// Provides methods for GameData database access
public interface GameDAO {

    public void createGameData(GameData gameData);

    public GameData readGameData(int gameID);

    public ArrayList<GameData> readAllGameData();

    public void updateGameData(GameData gameData);

    public void deleteGameData(int gameID);

    public void deleteAllGameData();
}
