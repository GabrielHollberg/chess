package dataaccess;

import model.UserData;

// Provides methods for UserData database access
public interface UserDAO {

    void createUserData(UserData userData);

    UserData readUserData(String username);

    void deleteAllUserData();
}
