package exceptionhandler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;

import java.util.Map;

public class BadRequestHandler implements ExceptionHandler<Exception> {

    @Override
    public void handle(Exception e, Context ctx) {
        String json = new Gson().toJson(Map.of("message", e.getMessage()));
        ctx.status(400);
        ctx.json(json);
    }
}
