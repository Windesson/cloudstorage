package com.udacity.jwdnd.course1.cloudstorage.dto;

public class CredentialDto {
    private Integer credentialId;
    private String password;
    private String username;
    private String encryptedPassword;
    private String url;

    public CredentialDto(Integer credentialId, String password, String username, String encryptedPassword, String url) {
        this.credentialId = credentialId;
        this.password = password;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.url = url;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
