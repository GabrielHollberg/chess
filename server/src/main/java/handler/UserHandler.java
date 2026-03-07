package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UserHandler implements Handler {

    public UserHandler() {}

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        var bodyObject = new Gson().fromJson(ctx.body(), Map.class);
        System.out.println(bodyObject.get("username"));
    }
}
