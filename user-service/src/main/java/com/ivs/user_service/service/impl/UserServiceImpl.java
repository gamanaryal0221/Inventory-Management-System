package com.ivs.user_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivs.user_service.domain.User;
import com.ivs.user_service.repository.UserRepository;
import com.ivs.user_service.service.UserService;
import com.ivs.user_service.service.AuthenticationService;
import com.ivs.user_service.service.AuthenticationService.PasswordDetails;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User createUser(User user) {
        String receivedPassword = user.getPassword();
        PasswordDetails passwordDetails = authenticationService.makePassword(receivedPassword);
        user.setSaltValue(passwordDetails.getSaltValue());
        user.setPassword(passwordDetails.getHashedPassword());

        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setMiddleName(user.getMiddleName());
            existingUser.setLastName(user.getLastName());
            existingUser.setMailAddress(user.getMailAddress());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}