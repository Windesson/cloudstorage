package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public UserService(UserMapper userMapper, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public int createUser(UserModel userModel) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encryptValue = encryptionService.encryptValue(userModel.getPassword(), encodedSalt);
        return userMapper.insert(new UserModel(null, userModel.getUsername(), encodedSalt, encryptValue, userModel.getFirstName(), userModel.getLastName()));
    }

    public Integer getUserId(String username) {
        return userMapper.getUser(username).getUserId();
    }
}