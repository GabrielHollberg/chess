package handler;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.server.Authentication;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.function.Consumer;

public class WSHandler implements Consumer<WsConfig> {

    private final GameService gameService;

    public WSHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void accept(WsConfig ws) {
        ws.onConnect(ctx -> {
            ctx.enableAutomaticPings();
            System.out.println("Websocket connected");
        });
        ws.onMessage(ctx -> handleUserGameCommand(ctx.message()));
        ws.onClose(ctx -> System.out.println("Websocket closed"));
    }

    public void handleUserGameCommand(String json) {
        UserGameCommand userGameCommand = new Gson().fromJson(json, UserGameCommand.class);

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            String gameJson = gameService.getGameJson(userGameCommand.getAuthToken(), userGameCommand.getGameID());
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        }
    }
}