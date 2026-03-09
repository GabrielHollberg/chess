package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class JsonSyntaxHandler implements ExceptionHandler<Exception> {

    @Override
    public void handle(@NotNull Exception e, Context ctx) {
        String json = new Gson().toJson(Map.of("message", "Error: bad request"));
        ctx.status(400);
        ctx.json(json);
    }
}
