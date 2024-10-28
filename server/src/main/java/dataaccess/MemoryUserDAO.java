package dataaccess;

import Model.AuthData;
import Model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> userDataList;

    public void clear() {
        userDataList.clear();
    }

    public void addUserData(String username, UserData userData) {
        userDataList.put(username, userData);
    }

    public void deleteUserData(String username) {
        userDataList.remove(username);
    }

    public UserData getUserData(String username) {
        return userDataList.get(username);
    }
}
