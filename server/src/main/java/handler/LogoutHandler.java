package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.util.Map;

public class LogoutHandler implements Handler {

    UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        userService.logoutUser(authToken);
        ctx.status(200);
        ctx.json(new Gson().toJson(Map.of()));
    }
}
