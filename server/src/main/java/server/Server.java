package server;

import exceptionHandler.*;
import dataaccess.*;
import exception.*;
import handler.*;
import io.javalin.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;

import java.util.HashMap;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Initialize all memory data structures
        HashMap<String, AuthData> auths = new HashMap<>();
        HashMap<String, UserData> users = new HashMap<>();
        HashMap<Integer, GameData> games = new HashMap<>();

        // Initialize all memory access objects
        AuthDAO authDAO = new MemoryAuthDAO(auths);
        UserDAO userDAO = new MemoryUserDAO(users);
        GameDAO gameDAO = new MemoryGameDAO(games);

        // Initialize all Server Service objects
        AuthService authService = new AuthService(authDAO);
        UserService userService = new UserService(userDAO, authService);
        GameService gameService = new GameService(gameDAO, authService);

        // Initialize handler objects and route them to endpoints
        javalin.post("/user", new RegisterHandler(userService));
        javalin.post("/session", new LoginHandler(userService));
        javalin.delete("/session", new LogoutHandler(userService));
        javalin.post("/game", new CreateGameHandler(gameService));
        javalin.get("/game", new ListGamesHandler(gameService));
        javalin.put("/game", new JoinGameHandler(gameService));
        javalin.delete("/db", new ClearDatabaseHandler(authService, userService, gameService));

        // Initialize exception handler objects
        javalin.exception(BadRequestException.class, new BadRequestHandler());
        javalin.exception(UsernameTakenException.class, new UsernameTakenHandler());
        javalin.exception(TeamColorTakenException.class, new teamColorTakenHandler());
        javalin.exception(AuthTakenException.class, new teamColorTakenHandler());
        javalin.exception(UnauthorizedException.class, new UnauthorizedHandler());
        javalin.exception(Exception.class, new OtherExceptionsHandler());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
