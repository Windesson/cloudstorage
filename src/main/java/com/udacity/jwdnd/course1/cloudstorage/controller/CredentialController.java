package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CredentialController {

    CredentialService credentialService;
    UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping(value = "/credential")
    public String newCredential(Authentication authentication, CredentialDto CredentialDto){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            if(CredentialDto.getCredentialId() == null) credentialService.addCredential(CredentialDto,userId);
            else credentialService.updateCredential(CredentialDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }

    @PostMapping(value = "/credential/delete/{id}")
    public String deleteNote(Authentication authentication, @PathVariable(value = "id") Integer credentialId){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            credentialService.deleteCredential(credentialId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }
}
