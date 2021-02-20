package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import com.udacity.jwdnd.course1.cloudstorage.model.Messenger;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Controller

public class FileController {

    UserService userService;
    FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/file/upload")
    public RedirectView uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file,
                             RedirectAttributes model){
        try {
            Integer userId = userService.getUserId(authentication.getName());

            if(file.getOriginalFilename().isEmpty()){
                model.addFlashAttribute("error", Messenger.FILE_NAME_EMPTY);
            }
            else if(fileService.fileExists(file.getOriginalFilename(), userId)){
                model.addFlashAttribute("error", Messenger.FILE_NAME_DUPLICATED);
            } else{
                fileService.add(file, userId);
                model.addFlashAttribute("success",true);
            }
        } catch (Exception e) {
            model.addFlashAttribute("error", Messenger.ERROR_DEFAULT);
        }

        RedirectView redirectView= new RedirectView("/home",true);
        return redirectView;
    }

    @PostMapping(value = "/file/delete/{id}")
    public RedirectView deleteFile(Authentication authentication, @PathVariable(value = "id") Integer fileId, RedirectAttributes model){

        try {
            Integer userId = userService.getUserId(authentication.getName());
            fileService.delete(fileId, userId);
            model.addFlashAttribute("success",true);
        } catch (Exception e) {
            model.addFlashAttribute("error", Messenger.ERROR_DEFAULT);
        }

        RedirectView redirectView= new RedirectView("/home",true);
        return redirectView;
    }

    @GetMapping(value = "/file/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getFile(Authentication authentication, HttpServletResponse response,
                                         @PathVariable(value = "id") Integer fileId, Model model){
        byte[] data = new byte[0];
        
        try {
            Integer userId = userService.getUserId(authentication.getName());
            FileModel fileModel = fileService.getFileById(fileId, userId);
            data = fileModel.getFiledata();
            String headerString = "attachment; filename=\"" + fileModel.getFilename() + "\"";
            response.setHeader("Content-Disposition", headerString);
        } catch (Exception e) {
            model.addAttribute("error", Messenger.ERROR_DEFAULT);
        }

        return data;
    }
}
