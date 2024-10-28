package dataaccess;

import Model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> authDataList;

    public void clear() {
        authDataList.clear();
    }

    public void addAuthData(String username, AuthData authData) {
        authDataList.put(username, authData);
    }

    public void deleteAuthData(String username) {
        authDataList.remove(username);
    }

    public AuthData getAuthData(String username) {
        return authDataList.get(username);
    }
}
