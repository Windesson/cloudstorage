package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<CredentialModel> getCredentials(Integer userId){
        List<CredentialModel> CredentialModelList = new ArrayList<>();

        for(CredentialModel model : this.credentialMapper.getCredentials(userId)){
            decryptPassword(model);
            CredentialModelList.add(model);
        }

        return CredentialModelList;
    }

    public Integer addCredential(CredentialModel credentialModel) {
        encryptPassword(credentialModel);
        credentialModel.setCredentialId(null);
        return this.credentialMapper.addCredential(credentialModel);
    }

    public Integer updateCredential(CredentialModel credentialModel){
        encryptPassword(credentialModel);
        return this.credentialMapper.updateCredential(credentialModel);
    }

    public Integer deleteCredential(Integer credentialId, Integer userId){
        return this.credentialMapper.deleteCredential(credentialId, userId);
    }

    private void encryptPassword(CredentialModel model) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        String plainPassword = model.getPassword();
        String key = encodedSalt;
        String encryptValue = encryptionService.encryptValue(plainPassword,key);

        model.setPassword(encryptValue);
        model.setKey(key);
    }

    private void decryptPassword(CredentialModel model) {
        String encryptedPassword = model.getPassword();
        String key = model.getKey();
        String encryptValue = encryptionService.decryptValue(encryptedPassword,key);
        model.setPassword(encryptValue);
    }
}
