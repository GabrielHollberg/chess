package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.JoinGameRequest;
import service.GameService;

public class CheckIfGameExistsHandler implements Handler {

    GameService gameService;

    public CheckIfGameExistsHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() <= 0 || joinGameRequest.playerColor().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        gameService.getGameData(authToken, joinGameRequest.gameID());
        ctx.status(200);
    }
}
