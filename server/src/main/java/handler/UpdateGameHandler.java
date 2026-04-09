package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.JoinGameRequest;
import request.LeaveGameRequest;
import request.UpdateGameRequest;
import service.GameService;

public class UpdateGameHandler implements Handler {
    GameService gameService;

    public UpdateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        UpdateGameRequest updateGameRequest = new Gson().fromJson(ctx.body(), UpdateGameRequest.class);
        if (updateGameRequest.gameID() <= 0) {
            throw new BadRequestException("Error: bad request");
        }
        gameService.updateGame(authToken, updateGameRequest);
        ctx.status(200);
    }
}
