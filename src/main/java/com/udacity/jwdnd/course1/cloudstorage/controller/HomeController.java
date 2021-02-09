package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String loginView(Authentication authentication, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        model.addAttribute("files", this.fileService.getFiles(userId));

        return "home";
    }

    @PostMapping
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            fileService.add(file, userId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }
}