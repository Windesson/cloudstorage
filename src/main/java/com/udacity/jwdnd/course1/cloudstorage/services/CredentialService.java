package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
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

    public List<CredentialDto> getCredentials(Integer userId){
        List<CredentialDto> CredentialList = new ArrayList<>();

        for(CredentialModel model : this.credentialMapper.getCredentials(userId)){
            CredentialList.add(
                    new CredentialDto(
                            model.getCredentialId(),
                            encryptionService.decryptValue(model.getPassword(), model.getKey()),
                            model.getUsername(),
                            model.getPassword(),
                            model.getUrl()
                    ));
        }

        return CredentialList;
    }

    public Integer addCredential(CredentialDto credentialDto, Integer userID) {
        CredentialModel credentialModel = getCredentialModel(credentialDto, userID);
        credentialModel.setCredentialId(null);

        return this.credentialMapper.addCredential(credentialModel);
    }

    public Integer updateCredential(CredentialDto credentialDto, Integer userID){
        CredentialModel credentialModel = getCredentialModel(credentialDto, userID);
        credentialModel.setCredentialId(credentialDto.getCredentialId());

        return this.credentialMapper.updateCredential(credentialModel);
    }

    private CredentialModel getCredentialModel(CredentialDto credentialDto, Integer userID) {
        CredentialModel credentialModel = new CredentialModel();
        credentialModel.setUsername(credentialDto.getUsername());
        credentialModel.setPassword(credentialDto.getPassword());
        credentialModel.setUrl(credentialDto.getUrl());
        credentialModel.setUserId(userID);
        encryptPassword(credentialModel);
        return credentialModel;
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
}
