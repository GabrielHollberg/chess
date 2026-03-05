package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO {

    public HashMap<String,AuthData> auths;

    void createAuth(AuthData authData) throws DataAccessException {
        auths.put(authData.authToken(), authData);
    }

    AuthData readAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    void updateAuth(AuthData authData) throws DataAccessException {
        auths.remove(authData.authToken());
        auths.put(authData.authToken(), authData);
    }

    void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }
}
