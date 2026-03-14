package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import model.LightGameData;
import org.jetbrains.annotations.NotNull;
import service.GameService;

import java.util.List;
import java.util.Map;

public class ListGamesHandler implements Handler {

    GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        List<LightGameData> games = gameService.listGames(authToken);
        ctx.status(200);
        ctx.json(new Gson().toJson(Map.of("games", games)));
    }
}
