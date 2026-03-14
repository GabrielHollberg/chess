package service;

import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.UserData;

public class UserService {

    private final UserDAO userDAO;
    private final AuthService authService;

    public UserService (UserDAO userDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.authService = authService;
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws DataAccessException {
        if (userDAO.readUserData(registerRequest.username()) == null) {
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.createUserData(userData);
            String authToken = authService.createAuthToken();
            authService.createAuth(authToken, registerRequest.username());
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new UsernameTakenException("Error: already taken");
        }
    }

    public LoginResult loginUser(LoginRequest loginRequest) throws DataAccessException {
        if (userDAO.readUserData(loginRequest.username()) != null) {
            UserData userData = userDAO.readUserData(loginRequest.username());
            if (userData.password().equals(loginRequest.password())) {
                String authToken = authService.createAuthToken();
                return new LoginResult(userData.username(), authToken);
            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void logoutUser(String authToken) throws DataAccessException {
        if (authService.authenticateUser(authToken)) {
            authService.deleteAuthData(authToken);
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void deleteAllUserData() {
        userDAO.deleteAllUserData();
    }
}
