package dataaccess;

import model.GameData;

public interface GameDAO {

    void createGame(GameData gameData) throws DataAccessException;

    GameData readGame(int gameID) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    void deleteGame(int gameID) throws DataAccessException;
}
