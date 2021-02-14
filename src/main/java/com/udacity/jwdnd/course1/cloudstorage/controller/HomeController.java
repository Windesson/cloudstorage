package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
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

    public HomeController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, Model model) {
        String username = authentication.getName();
        if(username!= null) {
            Integer userId = userService.getUserId(username);
            model.addAttribute("files", this.fileService.getFiles(userId));
            return "home";
        }
        return "redirect:/logout";
    }
}