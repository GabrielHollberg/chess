package handler;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import model.GameData;
import request.UpdateGameRequest;
import service.AuthService;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import static ui.EscapeSequences.EMPTY;

public class WSHandler implements Consumer<WsConfig> {

    private final GameService gameService;
    private final AuthService authService;
    private final HashMap<Integer, ArrayList<WsContext>> wsContexts = new HashMap<>();

    public WSHandler(AuthService authService, GameService gameService) {
        this.gameService = gameService;
        this.authService = authService;
    }

    @Override
    public void accept(WsConfig ws) {
        ws.onConnect(ctx -> {
            ctx.enableAutomaticPings();
            System.out.println("Websocket connected");
        });
        ws.onMessage(this::handleUserGameCommand);
        ws.onClose(ctx -> {
            for (ArrayList<WsContext> list : wsContexts.values()) {
                list.remove(ctx);
            }
            System.out.println("Websocket closed");
        });
    }

    public void handleUserGameCommand(WsMessageContext ctx) {
        UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            if (!wsContexts.containsKey(userGameCommand.getGameID())) {
                ArrayList<WsContext> list = new ArrayList<>();
                list.add(ctx);
                wsContexts.put(userGameCommand.getGameID(), list);
            } else {
                wsContexts.get(userGameCommand.getGameID()).add(ctx);
            }
            GameData gameData;
            String gameJson = "";
            try {
                String username = authService.getUsername(userGameCommand.getAuthToken());
                gameData = gameService.getGameData(userGameCommand.getAuthToken(), userGameCommand.getGameID());
                gameJson = new Gson().toJson(gameData.game());
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson, null, null);
                ctx.send(new Gson().toJson(serverMessage));
                String message;
                if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                    message = "\n" + EMPTY + "  ------------Chess------------      " + username + " joined the game as white!";
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                    message = "\n" + EMPTY + "  ------------Chess------------      " + username + " joined the game as black!";
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                } else {
                    message = "\n" + EMPTY + "  ------------Chess------------      " + username + " is now spectating!";
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                }
                ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                for (WsContext wsContext : list) {
                    if (!wsContext.equals(ctx)) {
                        wsContext.send(new Gson().toJson(serverMessage));
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "Error");
                ctx.send(new Gson().toJson(serverMessage));
            }
        } else if (userGameCommand.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            try {
                GameData gameData = gameService.getGameData(userGameCommand.getAuthToken(), userGameCommand.getGameID());
                String username = authService.getUsername(userGameCommand.getAuthToken());
                String message;
                if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                    message = "\n" + EMPTY + "  ------------Chess------------      " + username + " left the game!";
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                    ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                    for (WsContext wsContext : list) {
                        if (!wsContext.sessionId().equals(ctx.sessionId())) {
                            wsContext.send(new Gson().toJson(serverMessage));
                        }
                    }
                } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                    message = "\n" + EMPTY + "  ------------Chess------------      " + username + " left the game!";
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                    ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                    for (WsContext wsContext : list) {
                        if (!wsContext.sessionId().equals(ctx.sessionId())) {
                            wsContext.send(new Gson().toJson(serverMessage));
                        }
                    }
                } else {
                    message = "\n" + EMPTY + "  ------------Chess------------      " + username + " stopped spectating!";
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                    ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                    for (WsContext wsContext : list) {
                        if (!wsContext.sessionId().equals(ctx.sessionId())) {
                            wsContext.send(new Gson().toJson(serverMessage));
                        }
                    }
                }
                wsContexts.get(userGameCommand.getGameID()).remove(ctx);
                gameService.leaveGame(userGameCommand);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "Error");
                ctx.send(new Gson().toJson(serverMessage));
            }
        } else if (userGameCommand.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            try {
                String username = authService.getUsername(userGameCommand.getAuthToken());
                GameData gameData = gameService.getGameData(userGameCommand.getAuthToken(), userGameCommand.getGameID());
                ChessGame chessGame = gameData.game();
                if (chessGame.getGameOver()) {
                    throw new Exception();
                }
                Collection<ChessMove> validMoves = chessGame.validMoves(userGameCommand.getChessMove().getStartPosition());
                if (gameData.whiteUsername().equals(username)) {
                    if (chessGame.getBoard().getPiece(userGameCommand.getChessMove().getStartPosition()).getTeamColor() == ChessGame.TeamColor.WHITE && validMoves.contains(userGameCommand.getChessMove()) && chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                        char startPositionLetter;
                        switch (userGameCommand.getChessMove().getStartPosition().getColumn()) {
                            case 0:
                                startPositionLetter = 'A';
                                break;
                            case 1:
                                startPositionLetter = 'B';
                                break;
                            case 2:
                                startPositionLetter = 'C';
                                break;
                            case 3:
                                startPositionLetter = 'D';
                                break;
                            case 4:
                                startPositionLetter = 'E';
                                break;
                            case 5:
                                startPositionLetter = 'F';
                                break;
                            case 6:
                                startPositionLetter = 'G';
                                break;
                            case 7:
                                startPositionLetter = 'H';
                                break;
                            default:
                                startPositionLetter = 'A';
                                break;
                        }
                        char endPositionLetter;
                        switch (userGameCommand.getChessMove().getStartPosition().getColumn()) {
                            case 0:
                                endPositionLetter = 'A';
                                break;
                            case 1:
                                endPositionLetter = 'B';
                                break;
                            case 2:
                                endPositionLetter = 'C';
                                break;
                            case 3:
                                endPositionLetter = 'D';
                                break;
                            case 4:
                                endPositionLetter = 'E';
                                break;
                            case 5:
                                endPositionLetter = 'F';
                                break;
                            case 6:
                                endPositionLetter = 'G';
                                break;
                            case 7:
                                endPositionLetter = 'H';
                                break;
                            default:
                                endPositionLetter = 'A';
                                break;
                        }
                        String message = "\n" + EMPTY + "  ------------Chess------------      " + username + " moved from " + startPositionLetter + (userGameCommand.getChessMove().getStartPosition().getRow() + 1) + " to " + endPositionLetter + (userGameCommand.getChessMove().getEndPosition().getRow() + 1);
                        ServerMessage serverMessage;
                        chessGame.makeMove(userGameCommand.getChessMove());
                        ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                        serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                        for (WsContext wsContext : list) {
                            if (!wsContext.equals(ctx)) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        }
                        if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                            chessGame.setGameOver(true);
                            message = "\n" + EMPTY + "  ------------Chess------------      " + gameData.blackUsername() + " lost by checkmate!";
                            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            for (WsContext wsContext : list) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        } else if (chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
                            chessGame.setGameOver(true);
                            message = "\n" + EMPTY + "  ------------Chess------------      " + gameData.whiteUsername() + " and " + gameData.blackUsername() + "drew by stalemate!";
                            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            for (WsContext wsContext : list) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        } else if (chessGame.isInCheck(ChessGame.TeamColor.BLACK)) {
                            chessGame.setGameOver(true);
                            message = "\n" + EMPTY + "  ------------Chess------------      " + gameData.blackUsername() + " is in check!";
                            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            for (WsContext wsContext : list) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        }
                        String gameJson = new Gson().toJson(chessGame);
                        UpdateGameRequest updateGameRequest = new UpdateGameRequest(userGameCommand.getGameID(), chessGame);
                        gameService.updateGame(userGameCommand.getAuthToken(), updateGameRequest);
                        serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson, null, null);
                        for (WsContext wsContext : list) {
                            wsContext.send(new Gson().toJson(serverMessage));
                        }
                    } else {
                        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "Error");
                        ctx.send(new Gson().toJson(serverMessage));
                    }
                } else {
                    if (chessGame.getBoard().getPiece(userGameCommand.getChessMove().getStartPosition()).getTeamColor() == ChessGame.TeamColor.BLACK && validMoves.contains(userGameCommand.getChessMove()) && chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                        char startPositionLetter;
                        switch (userGameCommand.getChessMove().getStartPosition().getColumn()) {
                            case 0:
                                startPositionLetter = 'A';
                                break;
                            case 1:
                                startPositionLetter = 'B';
                                break;
                            case 2:
                                startPositionLetter = 'C';
                                break;
                            case 3:
                                startPositionLetter = 'D';
                                break;
                            case 4:
                                startPositionLetter = 'E';
                                break;
                            case 5:
                                startPositionLetter = 'F';
                                break;
                            case 6:
                                startPositionLetter = 'G';
                                break;
                            case 7:
                                startPositionLetter = 'H';
                                break;
                            default:
                                startPositionLetter = 'A';
                                break;
                        }
                        char endPositionLetter;
                        switch (userGameCommand.getChessMove().getStartPosition().getColumn()) {
                            case 0:
                                endPositionLetter = 'A';
                                break;
                            case 1:
                                endPositionLetter = 'B';
                                break;
                            case 2:
                                endPositionLetter = 'C';
                                break;
                            case 3:
                                endPositionLetter = 'D';
                                break;
                            case 4:
                                endPositionLetter = 'E';
                                break;
                            case 5:
                                endPositionLetter = 'F';
                                break;
                            case 6:
                                endPositionLetter = 'G';
                                break;
                            case 7:
                                endPositionLetter = 'H';
                                break;
                            default:
                                endPositionLetter = 'A';
                                break;
                        }
                        String message = "\n" + EMPTY + "  ------------Chess------------      " + username + " moved from " + startPositionLetter + (userGameCommand.getChessMove().getStartPosition().getRow() + 1) + " to " + endPositionLetter + (userGameCommand.getChessMove().getEndPosition().getRow() + 1);
                        ServerMessage serverMessage;
                        chessGame.makeMove(userGameCommand.getChessMove());
                        chessGame.setTeamTurn(ChessGame.TeamColor.WHITE);
                        ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                        serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                        for (WsContext wsContext : list) {
                            if (!wsContext.equals(ctx)) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        }
                        if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                            chessGame.setGameOver(true);
                            message = "\n" + EMPTY + "  ------------Chess------------      " + gameData.whiteUsername() + " lost by checkmate!";
                            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            for (WsContext wsContext : list) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        } else if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
                            chessGame.setGameOver(true);
                            message = "\n" + EMPTY + "  ------------Chess------------      " + gameData.blackUsername() + " and " + gameData.whiteUsername() + "drew by stalemate!";
                            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            for (WsContext wsContext : list) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        } else if (chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
                            message = "\n" + EMPTY + "  ------------Chess------------      " + gameData.whiteUsername() + " is in check!";
                            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            for (WsContext wsContext : list) {
                                wsContext.send(new Gson().toJson(serverMessage));
                            }
                        }
                        String gameJson = new Gson().toJson(chessGame);
                        UpdateGameRequest updateGameRequest = new UpdateGameRequest(userGameCommand.getGameID(), chessGame);
                        gameService.updateGame(userGameCommand.getAuthToken(), updateGameRequest);
                        serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson, null, null);
                        for (WsContext wsContext : list) {
                            wsContext.send(new Gson().toJson(serverMessage));
                        }
                    } else {
                        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "Error");
                        ctx.send(new Gson().toJson(serverMessage));
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "Error");
                ctx.send(new Gson().toJson(serverMessage));
            }
        } else if (userGameCommand.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            try {
                GameData gameData = gameService.getGameData(userGameCommand.getAuthToken(), userGameCommand.getGameID());
                if (gameData.game().getGameOver()) {
                    throw new Exception();
                }
                String username = authService.getUsername(userGameCommand.getAuthToken());
                if ((gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) || (gameData.blackUsername() != null && gameData.blackUsername().equals(username))) {
                    gameData.game().setGameOver(true);
                    UpdateGameRequest updateGameRequest = new UpdateGameRequest(userGameCommand.getGameID(), gameData.game());
                    gameService.updateGame(userGameCommand.getAuthToken(), updateGameRequest);
                    String selfMessage = "\n" + EMPTY + "  ------------Chess------------      You forfeited the game!";
                    String message = "\n" + EMPTY + "  ------------Chess------------      " + username + " forfeited the game!";
                    ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                    for (WsContext wsContext : list) {
                        if (wsContext.sessionId().equals(ctx.sessionId())) {
                            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, selfMessage, null);
                            wsContext.send(new Gson().toJson(serverMessage));
                        } else {
                            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                            wsContext.send(new Gson().toJson(serverMessage));
                        }
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "Error");
                ctx.send(new Gson().toJson(serverMessage));
            }
        }
    }
}