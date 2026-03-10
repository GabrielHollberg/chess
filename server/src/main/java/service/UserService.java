package service;

import request.RegisterRequest;
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
            String authToken = authService.generateToken();
            authService.createAuth(authToken, registerRequest.username());
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new UsernameTakenException("Error: already taken");
        }
    }

    public void deleteAllUserData() {
        userDAO.deleteAllUserData();
    }
}
