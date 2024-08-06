package com.ivs.user_service.service;

import java.util.List;
import com.ivs.user_service.domain.User;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(String id);
    User createUser(User user);
    User updateUser(String id, User user);
    void deleteUser(String id);
    List<User> getUserByIdInList(List<String> userIdList);
}
