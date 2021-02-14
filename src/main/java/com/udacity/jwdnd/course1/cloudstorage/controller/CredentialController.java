package com.udacity.jwdnd.course1.cloudstorage.controller;

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

    @PostMapping(value = "/credential/new")
    public String newCredential(Authentication authentication, CredentialModel credentialModel){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            credentialModel.setUserId(userId);
            credentialService.addCredential(credentialModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }

    @RequestMapping(value = "/credential/edit", method = RequestMethod.PATCH)
    public String editCredential(Authentication authentication, CredentialModel credentialModel){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            credentialModel.setUserId(userId);
            credentialService.updateCredential(credentialModel);
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
