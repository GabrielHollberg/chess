package dataaccess;

import Model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> data;

    public void clear() {
        data.clear();
    }
}
