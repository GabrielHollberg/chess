package service;

import dataaccess.*;
import exception.AuthTakenException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

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
    GameService gameService = new GameService(gameDAO, authService);

    @Test
    @DisplayName("Valid Registration")
    public void registerUserPositiveTest() throws DataAccessException {
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
    @DisplayName("Invalid Registration")
    public void registerUserNegativeTest() throws DataAccessException {
        userService.registerUser(registerRequest);
        userService.registerUser(registerRequest2);
        assertThrows(DataAccessException.class, () -> userService.registerUser(registerRequest));
        assertThrows(DataAccessException.class, () -> userService.registerUser(registerRequest2));
    }

    @Test
    @DisplayName("Valid Login")
    public void loginUserPositiveTest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(registerRequest);
        authDAO.deleteAuthData(registerResult.authToken());
        assertNull(authDAO.readAuthData(registerResult.authToken()));
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        assertDoesNotThrow(() -> userService.loginUser(loginRequest));
    }

    @Test
    @DisplayName("Invalid Login")
    public void loginUserNegativeTest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(registerRequest);
        authDAO.deleteAuthData(registerResult.authToken());
        assertNull(authDAO.readAuthData(registerResult.authToken()));
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), "Invalid Password");
        assertThrows(UnauthorizedException.class, () -> userService.loginUser(loginRequest));
        LoginRequest loginRequest2 = new LoginRequest("Invalid Username", registerRequest.password());
        assertThrows(UnauthorizedException.class, () -> userService.loginUser(loginRequest2));
    }

    @Test
    @DisplayName("Valid Logout")
    public void logoutUserPositiveTest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(registerRequest);
        assertNotNull(authDAO.readAuthData(registerResult.authToken()));
        assertDoesNotThrow(() -> userService.logoutUser(registerResult.authToken()));
        assertNull(authDAO.readAuthData(registerResult.authToken()));
    }

    @Test
    @DisplayName("Invalid Logout")
    public void logoutUserNegativeTest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(registerRequest);
        assertNotNull(authDAO.readAuthData(registerResult.authToken()));
        assertDoesNotThrow(() -> userService.logoutUser(registerResult.authToken()));
        assertNull(authDAO.readAuthData(registerResult.authToken()));
        assertThrows(UnauthorizedException.class, () -> userService.logoutUser(registerResult.authToken()));
    }

    @Test
    @DisplayName("Clear Database")
    public void clearDatabasePositiveTest() throws DataAccessException {
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

    @Test
    @DisplayName("Create Auth Valid")
    public void createAuthPositiveTest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(registerRequest);
        String authToken = authService.createAuthToken();
        assertDoesNotThrow(() -> authService.createAuth(authToken, registerResult.username()));
        assertNotNull(authDAO.readAuthData(authToken));
    }

    @Test
    @DisplayName("Create Auth Invalid")
    public void createAuthNegativeTest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(registerRequest);
        String authToken = authService.createAuthToken();
        assertDoesNotThrow(() -> authService.createAuth(authToken, "Invalid Username"));
        assertThrows(AuthTakenException.class, () -> authService.createAuth(authToken, "Invalid Username"));

    }
}
