package com.example.dailyexpenses.service;

import com.example.dailyexpenses.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    boolean deleteUserById(Long id);
    void deleteAllUsers();
}
