package service;

import chess.ChessGame;
import exception.DataAccessException;
import dataaccess.GameDAO;
import exception.BadRequestException;
import exception.TeamColorTakenException;
import exception.UnauthorizedException;
import model.GameData;
import model.LightGameData;
import request.CreateGameRequest;
import request.JoinGameRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
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

    public List<LightGameData> listGames(String authToken) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            Map<Integer,GameData> games = gameDAO.readAllGameData();
            List<LightGameData> lightGames = new ArrayList<>();
            for (GameData game : games.values()) {
                lightGames.add(new LightGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }
            return lightGames;
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            if (gameDAO.readGameData(joinGameRequest.gameID()) != null) {
                GameData gameData = gameDAO.readGameData(joinGameRequest.gameID());
                if (joinGameRequest.playerColor().equals("WHITE")) {
                    if (gameData.whiteUsername() == null) {
                        GameData updatedGameData = new GameData(gameData.gameID(), authService.getUsername(authToken), gameData.blackUsername(), gameData.gameName(), gameData.game());
                        gameDAO.updateGameData(updatedGameData);
                    } else {
                        throw new TeamColorTakenException("Error: already taken");
                    }
                } else if (joinGameRequest.playerColor().equals("BLACK")) {
                    if (gameData.blackUsername() == null) {
                        GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authService.getUsername(authToken), gameData.gameName(), gameData.game());
                        gameDAO.updateGameData(updatedGameData);
                    } else {
                        throw new TeamColorTakenException("Error: already taken");
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

    public void deleteAllGameData() {
        gameDAO.deleteAllGameData();
    }
}
