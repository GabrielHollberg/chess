package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void createAuth(String authToken, String username) throws DataAccessException {
        AuthData authData = new AuthData(authToken, username);
        authDAO.createAuthData(authData);
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

    public void deleteAuthData(String authToken) {
        authDAO.deleteAuthData(authToken);
    }

    public void deleteAllAuthData() {
        authDAO.deleteAllAuthData();
    }
}
