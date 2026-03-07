package handler;

import com.google.gson.Gson;
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
        RegisterResult registerResult = userService.registerUser(registerRequest);
        String json = new Gson().toJson(registerResult);
        ctx.json(json);
    }
}
