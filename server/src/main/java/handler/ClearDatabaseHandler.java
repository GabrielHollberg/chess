package handler;

import service.AuthService;
import service.GameService;
import service.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class ClearDatabaseHandler implements Handler {

    AuthService authService;
    UserService userService;
    GameService gameService;

    public ClearDatabaseHandler(AuthService authService, UserService userService, GameService gameService) {
        this.authService = authService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        authService.deleteAllAuthData();
        userService.deleteAllUserData();
        gameService.deleteAllGameData();
        ctx.status(200);
    }
}
