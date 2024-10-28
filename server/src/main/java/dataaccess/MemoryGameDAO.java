package dataaccess;

import Model.GameData;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> gameList;

    public void clear() {
        gameList.clear();
    }
}
