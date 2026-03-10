package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    @DisplayName("Valid Registration")
    public void registerUserPositiveTest() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("Name", "Password", "Email");
        HashMap<String, UserData> users = new HashMap<>();
        HashMap<String, AuthData> auths = new HashMap<>();
        UserDAO userDAO = new MemoryUserDAO(users);
        AuthDAO authDAO = new MemoryAuthDAO(auths);
        AuthService authService = new AuthService(authDAO);
        UserService userService = new UserService(userDAO, authService);
        RegisterResult registerResult = userService.registerUser(registerRequest);
        UserData userData = userDAO.readUserData("Name");
        assertEquals("Name", userData.username());
        assertEquals("Password", userData.password());
        assertEquals("Email", userData.email());
        AuthData authData = authDAO.readAuthData(registerResult.authToken());
        assertEquals(registerResult.authToken(), authData.authToken());
        assertEquals("Name", authData.username());
    }

    @Test
    @DisplayName("Username Taken")
    public void registerUserNegativeTest() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("Name", "Password", "Email");
        RegisterRequest registerRequest2 = new RegisterRequest("Name2", "Password2", "Email2");
        HashMap<String, UserData> users = new HashMap<>();
        HashMap<String, AuthData> auths = new HashMap<>();
        UserDAO userDAO = new MemoryUserDAO(users);
        AuthDAO authDAO = new MemoryAuthDAO(auths);
        AuthService authService = new AuthService(authDAO);
        UserService userService = new UserService(userDAO, authService);
        userService.registerUser(registerRequest);
        userService.registerUser(registerRequest2);
        assertThrows(DataAccessException.class, () -> userService.registerUser(registerRequest));
        assertThrows(DataAccessException.class, () -> userService.registerUser(registerRequest2));
    }

    @Test
    @DisplayName("Clear Database")
    public void ClearDatabasePositiveTest() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("Name", "Password", "Email");
        RegisterRequest registerRequest2 = new RegisterRequest("Name2", "Password2", "Email2");
        HashMap<String, UserData> users = new HashMap<>();
        HashMap<String, AuthData> auths = new HashMap<>();
        HashMap<Integer, GameData> games = new HashMap<>();
        UserDAO userDAO = new MemoryUserDAO(users);
        AuthDAO authDAO = new MemoryAuthDAO(auths);
        GameDAO gameDAO = new MemoryGameDAO(games);
        AuthService authService = new AuthService(authDAO);
        UserService userService = new UserService(userDAO, authService);
        GameService gameService = new GameService(gameDAO);
        RegisterResult registerResult = userService.registerUser(registerRequest);
        RegisterResult registerResult2 = userService.registerUser(registerRequest2);
        authService.deleteAllAuthData();
        userService.deleteAllUserData();
        gameService.deleteAllGameData();
        assertNull(userDAO.readUserData("Name"));
        assertNull(userDAO.readUserData("Name"));
        assertNull(authDAO.readAuthData(registerResult.authToken()));
        assertNull(authDAO.readAuthData(registerResult2.authToken()));
    }
}
