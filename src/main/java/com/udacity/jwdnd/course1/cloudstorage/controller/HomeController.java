package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    UserService userService;
    FileService fileService;
    NoteService noteService;
    CredentialService credentialService;

    public HomeController(UserService userService, FileService fileService,
                          NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            Integer userId = userService.getUserId(username);
            model.addAttribute("files", this.fileService.getFiles(userId));
            model.addAttribute("notes", this.noteService.getNotes(userId));
            model.addAttribute("credentialDTOs", this.credentialService.getCredentials(userId));
        } catch (Exception e){
            model.addAttribute("error", "An error occurred please try again later.");
        }

        return "home";
    }
}