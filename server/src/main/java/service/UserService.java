package service;

import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.UserData;

public class UserService {

    private UserDAO userDAO;

    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData user = new UserData(username, password, email);
        userDAO.createUser(user);
    }

    public UserData readUser(String username) throws DataAccessException {
        return userDAO.readUser(username);
    }
}
