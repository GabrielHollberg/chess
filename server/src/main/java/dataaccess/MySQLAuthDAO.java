package dataaccess;

import model.AuthData;

import java.util.Map;

// Provides methods for AuthData memory access
public class MySQLAuthDAO implements AuthDAO {

    private final Map<String, AuthData> auths;

    public MySQLAuthDAO(Map<String, AuthData> auths) {
        this.auths = auths;
    }

    public void createAuthData(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    public AuthData readAuthData(String authToken) {
        return auths.get(authToken);
    }

    public void updateAuthData(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    public void deleteAuthData(String authToken) {
        auths.remove(authToken);
    }

    public void deleteAllAuthData() {
        auths.clear();
    }
}
