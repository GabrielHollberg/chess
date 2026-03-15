package exceptionHandler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class OtherExceptionsHandler implements ExceptionHandler<Exception> {

    @Override
    public void handle(@NotNull Exception e, @NotNull Context ctx) {
        String errorMessage = "Error: " + e.getMessage();
        String json = new Gson().toJson(Map.of("message", errorMessage));
        ctx.status(500);
        ctx.json(json);
    }
}
