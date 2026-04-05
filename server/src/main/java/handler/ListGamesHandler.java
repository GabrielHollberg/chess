package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import result.ListGamesResult;
import service.GameService;

public class ListGamesHandler implements Handler {

    GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        ListGamesResult listGamesResult = gameService.listGames(authToken);
        ctx.status(200);
        ctx.json(new Gson().toJson(listGamesResult));
    }
}
