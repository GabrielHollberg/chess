package handler;

import com.google.gson.Gson;
import exception.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import service.AuthService;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

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
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
                ctx.send(new Gson().toJson(serverMessage));
                String notification;
                if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                    notification = username + " joined the game as white!";
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
                } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                    notification = username + " joined the game as black!";
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
                } else {
                    notification = username + " joined the game as a spectator!";
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
                }
                ArrayList<WsContext> list = wsContexts.get(userGameCommand.getGameID());
                for (WsContext wsContext : list) {
                    if (!wsContext.equals(ctx)) {
                        wsContext.send(new Gson().toJson(serverMessage));
                    }
                }
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}