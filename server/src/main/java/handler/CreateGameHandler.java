package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.GameService;

import java.util.Map;

public class CreateGameHandler implements Handler {

    GameService gameService;

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        if (createGameRequest.gameName() != null && !createGameRequest.gameName().isBlank()) {
            CreateGameResult createGameResult = gameService.createGame(authToken, createGameRequest);
            ctx.status(200);
            ctx.json(new Gson().toJson(createGameResult));
        } else {
            throw new BadRequestException("Error: bad request");
        }
    }
}
