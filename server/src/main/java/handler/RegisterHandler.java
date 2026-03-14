package handler;

import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.RegisterRequest;
import result.RegisterResult;
import service.UserService;

public class RegisterHandler implements Handler {

    UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null || registerRequest.username().isBlank() || registerRequest.password().isBlank() || registerRequest.email().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        RegisterResult registerResult = userService.registerUser(registerRequest);
        ctx.json(new Gson().toJson(registerResult));
    }
}
