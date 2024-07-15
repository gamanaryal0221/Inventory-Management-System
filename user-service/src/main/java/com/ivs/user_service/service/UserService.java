package com.ivs.user_service.service;

import java.util.List;
import com.ivs.user_service.domain.User;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User login(String username, String password);
}
