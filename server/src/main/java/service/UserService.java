package service;

import dataaccess.MemoryUserDAO;

public class UserService {
    public void registerResult(RegisterRequest registerRequest) {}

    public void clear() {
        memoryUserDAO.clear();
    }

    MemoryUserDAO memoryUserDAO;
}
