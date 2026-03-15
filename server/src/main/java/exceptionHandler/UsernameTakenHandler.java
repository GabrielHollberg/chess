package exceptionHandler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UsernameTakenHandler implements ExceptionHandler<Exception> {

    @Override
    public void handle(@NotNull Exception e, @NotNull Context ctx) {
        String json = new Gson().toJson(Map.of("message", e.getMessage()));
        ctx.status(403);
        ctx.json(json);
    }
}
