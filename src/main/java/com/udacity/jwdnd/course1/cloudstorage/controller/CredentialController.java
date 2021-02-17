package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CredentialController {

    CredentialService credentialService;
    UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping(value = "/credential")
    public RedirectView newCredential(Authentication authentication, CredentialDto CredentialDto, RedirectAttributes model){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            if(CredentialDto.getCredentialId() == null) credentialService.addCredential(CredentialDto,userId);
            else credentialService.updateCredential(CredentialDto,userId);
        } catch (Exception e) {
            model.addFlashAttribute("error", "an error occurred please try again later");
        }

        RedirectView redirectView= new RedirectView("/home",true);
        return redirectView;
    }

    @PostMapping(value = "/credential/delete/{id}")
    public RedirectView deleteNote(Authentication authentication, @PathVariable(value = "id") Integer credentialId,
                             RedirectAttributes model){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            credentialService.deleteCredential(credentialId, userId);
        } catch (Exception e) {
            model.addFlashAttribute("error", "an error occurred please try again later");
        }

        RedirectView redirectView= new RedirectView("/home",true);
        return redirectView;
    }
}
