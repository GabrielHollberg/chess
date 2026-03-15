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

    // Create new AuthData record and add it to AuthData data structure
    public void createAuth(String authToken, String username) throws DataAccessException {
        if (authDAO.readAuthData(authToken) == null) {
            AuthData authData = new AuthData(authToken, username);
            authDAO.createAuthData(authData);
        } else {
            throw new AuthTakenException("auth already exists");
        }
    }

    // Create and return new authToken String
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

    // Return username String by reading AuthData by authToken
    public String getUsername(String authToken) {
        AuthData authData = authDAO.readAuthData(authToken);
        return authData.username();
    }

    // Delete AuthData by authToken
    public void deleteAuthData(String authToken) {
        authDAO.deleteAuthData(authToken);
    }

    // Delete all AuthData
    public void deleteAllAuthData() {
        authDAO.deleteAllAuthData();
    }
}
