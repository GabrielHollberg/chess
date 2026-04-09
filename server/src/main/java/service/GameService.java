package service;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.AlreadyTakenException;
import exception.DataAccessException;
import dataaccess.GameDAO;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LeaveGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;

public class GameService {

    GameDAO gameDAO;
    AuthService authService;

    public GameService(GameDAO gameDAO, AuthService authService) {
        this.gameDAO = gameDAO;
        this.authService = authService;
    }

    // Create new GameData Record with provided gameName String, new gameID, and new ChessGame object
    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            GameData gameData = new GameData(0, null, null, createGameRequest.gameName(), new ChessGame());
            int gameID = gameDAO.createGameData(gameData);
            return new CreateGameResult(gameID);
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    // Return list of GameData by reading all GameData with GameData access object
    public ListGamesResult listGames(String authToken) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            return new ListGamesResult(gameDAO.readAllGameData());
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public GameData getGameData(String authToken, int gameID) throws DataAccessException{
        if (authService.authenticateUser(authToken)) {
            return gameDAO.readGameData(gameID);
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    // Update GameData by searching by gameID with new username and teamColor
    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            if (gameDAO.readGameData(joinGameRequest.gameID()) != null) {
                GameData gameData = gameDAO.readGameData(joinGameRequest.gameID());
                if (joinGameRequest.playerColor().equals("WHITE")) {
                    if (gameData.whiteUsername() == null) {
                        GameData updatedGameData = new GameData(gameData.gameID(), authService.getUsername(authToken), gameData.blackUsername(), gameData.gameName(), gameData.game());
                        gameDAO.updateGameData(updatedGameData);
                    } else {
                        throw new AlreadyTakenException("Error: already taken");
                    }
                } else if (joinGameRequest.playerColor().equals("BLACK")) {
                    if (gameData.blackUsername() == null) {
                        GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authService.getUsername(authToken), gameData.gameName(), gameData.game());
                        gameDAO.updateGameData(updatedGameData);
                    } else {
                        throw new AlreadyTakenException("Error: already taken");
                    }
                } else {
                    throw new BadRequestException("Error: bad request");
                }
            } else {
                throw new BadRequestException("Error: bad request");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void leaveGame(String authToken, LeaveGameRequest leaveGameRequest) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            if (gameDAO.readGameData(leaveGameRequest.gameID()) != null) {
                GameData gameData = gameDAO.readGameData(leaveGameRequest.gameID());
                if (leaveGameRequest.playerColor().equals("WHITE")) {
                    GameData updatedGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
                    gameDAO.updateGameData(updatedGameData);
                } else if (leaveGameRequest.playerColor().equals("BLACK")) {
                    GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
                    gameDAO.updateGameData(updatedGameData);
                }
            } else {
                throw new BadRequestException("Error: bad request");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    // Delete all GameData
    public void deleteAllGameData() {
        gameDAO.deleteAllGameData();
    }
}
