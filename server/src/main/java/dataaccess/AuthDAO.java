package dataaccess;

import model.AuthData;

// Provides methods for AuthData database access
public interface AuthDAO {

    public void createAuthData(AuthData authData);

    public AuthData readAuthData(String authToken);

    public void updateAuthData(AuthData authData);

    public void deleteAuthData(String authToken);

    public void deleteAllAuthData();
}
