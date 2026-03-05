package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {

    private AuthDAO authDAO;

    public void createAuth(String authToken, String username) throws DataAccessException {
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);
    }
}
