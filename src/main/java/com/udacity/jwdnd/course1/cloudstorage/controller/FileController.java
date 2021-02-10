package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller

public class FileController {

    UserService userService;
    FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/file/upload")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            fileService.add(file, userId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }

    @RequestMapping(value = "/file/delete/{id}")
    public String deleteFile(Authentication authentication, @PathVariable(value = "id") Integer fileId){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            fileService.delete(fileId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }
}
