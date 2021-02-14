package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file){
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

    @GetMapping(value = "/file/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public  @ResponseBody byte[] getFile(Authentication authentication, HttpServletResponse response,
                                         @PathVariable(value = "id") Integer fileId){
        Integer userId = userService.getUserId(authentication.getName());

        byte[] data = new byte[0];
        
        try {
            FileModel fileModel = fileService.get(fileId, userId);
            data = fileModel.getFiledata();
            String headerString = "attachment; filename=\"" + fileModel.getFilename() + "\"";
            response.setHeader("Content-Disposition", headerString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    
}
