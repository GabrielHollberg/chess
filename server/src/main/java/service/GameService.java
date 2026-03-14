package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;

public class GameService {

    GameDAO gameDAO;
    AuthService authService;
    int IDCounter = 0;

    public GameService(GameDAO gameDAO, AuthService authService) {
        this.gameDAO = gameDAO;
        this.authService = authService;
    }

    public int createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            int gameID = createGameID();
            GameData gameData = new GameData(gameID, "", "", createGameRequest.gameName(), new ChessGame());
            gameDAO.createGameData(gameData);
            return gameID;
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public int createGameID() {
        IDCounter++;
        return IDCounter;
    }

    public void deleteAllGameData() {
        gameDAO.deleteAllGameData();
    }
}
