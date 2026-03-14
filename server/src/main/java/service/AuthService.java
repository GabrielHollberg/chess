package service;

import dataaccess.AuthDAO;
import exception.AuthTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void createAuth(String authToken, String username) throws DataAccessException {
        if (authDAO.readAuthData(authToken) == null) {
            AuthData authData = new AuthData(authToken, username);
            authDAO.createAuthData(authData);
        } else {
            throw new AuthTakenException("auth already exists");
        }
    }

    public String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    public boolean authenticateUser(String authToken) {
        if (authDAO.readAuthData(authToken) != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getUsername(String authToken) {
        AuthData authData = authDAO.readAuthData(authToken);
        return authData.username();
    }

    public void deleteAuthData(String authToken) {
        authDAO.deleteAuthData(authToken);
    }

    public void deleteAllAuthData() {
        authDAO.deleteAllAuthData();
    }
}
