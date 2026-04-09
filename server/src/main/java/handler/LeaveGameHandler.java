package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.JoinGameRequest;
import request.LeaveGameRequest;
import service.GameService;

public class LeaveGameHandler implements Handler {
    GameService gameService;

    public LeaveGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        LeaveGameRequest leaveGameRequest = new Gson().fromJson(ctx.body(), LeaveGameRequest.class);
        if (leaveGameRequest.gameID() <= 0) {
            throw new BadRequestException("Error: bad request");
        }
        gameService.leaveGame(authToken, leaveGameRequest);
        ctx.status(200);
    }
}
