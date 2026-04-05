package dataaccess;

import model.GameData;

import java.util.ArrayList;

// Provides methods for GameData database access
public interface GameDAO {

    int createGameData(GameData gameData);

    GameData readGameData(int gameID);

    ArrayList<GameData> readAllGameData();

    void updateGameData(GameData gameData);

    void deleteAllGameData();
}
