package com.example.crud.service;

import com.example.crud.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> getAllUsers();
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByUsername(String username);
    
    User createUser(User user);
    
    User updateUser(Long id, User userDetails);
    
    void deleteUser(Long id);
    
    boolean usernameExists(String username);
}
