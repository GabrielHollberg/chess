package dataaccess;

import model.UserData;

public interface UserDAO {

    public void createUserData(UserData userData);

    public UserData readUserData(String username);

    public void updateUserData(UserData userData);

    public void deleteUserData(String username);

    public void deleteAllUserData();
}
