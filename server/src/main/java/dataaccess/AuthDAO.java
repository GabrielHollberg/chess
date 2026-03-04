package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void createAuth(AuthData authData) throws DataAccessException;

    AuthData readAuth(String authToken) throws DataAccessException;

    void updateAuth(AuthData authData) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;
}
