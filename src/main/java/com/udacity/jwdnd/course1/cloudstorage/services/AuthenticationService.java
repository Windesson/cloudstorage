package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {
    private UserMapper userMapper;
    private EncryptionService encryptionService;

    public AuthenticationService(UserMapper userMapper, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserModel userModel = userMapper.getUser(username);
        if (userModel != null) {
            String encodedSalt = userModel.getSalt();
            String encryptValue = encryptionService.encryptValue(password, encodedSalt);
            if (userModel.getPassword().equals(encryptValue)) {
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}