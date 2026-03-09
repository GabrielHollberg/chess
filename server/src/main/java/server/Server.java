package server;

import dataaccess.*;
import handler.*;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import service.AuthService;
import service.GameService;
import service.UserService;
import service.UsernameTakenException;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        AuthService authService = new AuthService(authDAO);
        UserService userService = new UserService(userDAO, authService);

        javalin.post("/user", new RegisterHandler(userService));
        javalin.post("/session", new LoginHandler());
        javalin.delete("/session", new LogoutHandler());
        javalin.post("/game", new CreateGameHandler());
        javalin.get("/game", new ListGamesHandler());
        javalin.put("/game", new JoinGameHandler());
        javalin.delete("/db", new ClearDatabaseHandler());

        javalin.exception(UsernameTakenException.class, new UsernameTakenHandler());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
