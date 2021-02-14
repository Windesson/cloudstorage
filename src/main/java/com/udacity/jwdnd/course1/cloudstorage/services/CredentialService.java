package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<CredentialModel> getCredentials(Integer userId){
        return this.credentialMapper.getCredentials(userId);
    }

    public Integer addCredential(CredentialModel credentialModel) {
        return this.credentialMapper.addCredential(credentialModel);
    }

    public Integer updateCredential(CredentialModel credentialModel){
        return this.credentialMapper.updateCredential(credentialModel);
    }

    public Integer deleteCredential(Integer credentialId, Integer userId){
        return this.credentialMapper.deleteCredential(credentialId, userId);
    }
}
