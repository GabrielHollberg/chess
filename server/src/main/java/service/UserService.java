package service;

import exception.UnauthorizedException;
import exception.AlreadyTakenException;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import dataaccess.UserDAO;
import exception.DataAccessException;
import model.UserData;

public class UserService {

    private final UserDAO userDAO;
    private final AuthService authService;

    public UserService (UserDAO userDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.authService = authService;
    }

    // Create UserData record and AuthData record if provided username doesn't exist in UserData data structure
    public RegisterResult registerUser(RegisterRequest registerRequest) throws DataAccessException {
        if (userDAO.readUserData(registerRequest.username()) == null) {
            UserData userData = new UserData(registerRequest.username(), BCrypt.hashpw(registerRequest.password(),
                    BCrypt.gensalt()), registerRequest.email());
            userDAO.createUserData(userData);
            String authToken = authService.createAuthToken();
            authService.createAuth(authToken, registerRequest.username());
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
    }

    // Create AuthData record if provided username exists in UserData data structure and associated password matches provided password
    public LoginResult loginUser(LoginRequest loginRequest) throws DataAccessException {
        if (userDAO.readUserData(loginRequest.username()) != null) {
            UserData userData = userDAO.readUserData(loginRequest.username());
            if (BCrypt.checkpw(loginRequest.password(), userData.password())) {
                String authToken = authService.createAuthToken();
                authService.createAuth(authToken, userData.username());
                return new LoginResult(userData.username(), authToken);
            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    // Delete AuthData by searching by authToken
    public void logoutUser(String authToken) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            authService.deleteAuthData(authToken);
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    // Delete all UserData
    public void deleteAllUserData() {
        userDAO.deleteAllUserData();
    }
}
