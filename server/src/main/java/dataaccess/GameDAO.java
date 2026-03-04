package dataaccess;

import model.GameData;

public interface GameDAO {

    void createGame(GameData gameData) throws DataAccessException;

    GameData readGame(String gameID) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    void deleteGame(String gameID) throws DataAccessException;
}
