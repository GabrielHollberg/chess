package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.JoinGameRequest;
import service.GameService;

import java.util.Map;

public class JoinGameHandler implements Handler {

    GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() <= 0 || joinGameRequest.playerColor().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        gameService.joinGame(authToken, joinGameRequest);
        ctx.status(200);
    }
}
