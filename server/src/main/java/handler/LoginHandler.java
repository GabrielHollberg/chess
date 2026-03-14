package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.LoginRequest;
import result.LoginResult;
import service.UserService;

public class LoginHandler implements Handler {

    UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (loginRequest.username() == null || loginRequest.password() == null || loginRequest.username().isBlank() || loginRequest.password().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        LoginResult loginResult = userService.loginUser(loginRequest);
        ctx.status(200);
        ctx.json(new Gson().toJson(loginResult));
    }
}
