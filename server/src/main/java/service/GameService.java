package service;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import exception.AlreadyTakenException;
import exception.DataAccessException;
import dataaccess.GameDAO;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.UpdateGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import websocket.commands.UserGameCommand;

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
                if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                    if (gameData.whiteUsername() == null) {
                        GameData updatedGameData = new GameData(gameData.gameID(), authService.getUsername(authToken),
                                gameData.blackUsername(), gameData.gameName(), gameData.game());
                        gameDAO.updateGameData(updatedGameData);
                    } else {
                        throw new AlreadyTakenException("Error: already taken");
                    }
                } else if (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK) {
                    if (gameData.blackUsername() == null) {
                        GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                                authService.getUsername(authToken), gameData.gameName(), gameData.game());
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

    public void updateGame(String authToken, UpdateGameRequest updateGameRequest) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            if (gameDAO.readGameData(updateGameRequest.gameID()) != null) {
                GameData gameData = gameDAO.readGameData(updateGameRequest.gameID());
                GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                        gameData.blackUsername(), gameData.gameName(), updateGameRequest.chessGame());
                gameDAO.updateGameData(updatedGameData);
            } else {
                throw new BadRequestException("Error: bad request");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void leaveGame(UserGameCommand userGameCommand) throws DataAccessException {
        if (authService.authenticateUser(userGameCommand.getAuthToken())) {
            if (gameDAO.readGameData(userGameCommand.getGameID()) != null) {
                GameData gameData = gameDAO.readGameData(userGameCommand.getGameID());
                String username = authService.getUsername(userGameCommand.getAuthToken());
                if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                    GameData updatedGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(),
                            gameData.gameName(), gameData.game());
                    gameDAO.updateGameData(updatedGameData);
                } else {
                    GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null,
                            gameData.gameName(), gameData.game());
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
