package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String,AuthData> auths = new HashMap<>();

    public void createAuth(AuthData authData) throws DataAccessException {
        auths.put(authData.authToken(), authData);
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    public void updateAuth(AuthData authData) throws DataAccessException {
        auths.remove(authData.authToken());
        auths.put(authData.authToken(), authData);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }
}
