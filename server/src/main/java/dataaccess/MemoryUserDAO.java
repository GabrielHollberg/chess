package dataaccess;

import Model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> userList;

    public void clear() {
        userList.clear();
    }
}
