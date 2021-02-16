package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    NoteService noteService;
    UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(value = "/note")
    public String newNote(Authentication authentication, NoteModel noteModel){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            noteModel.setUserId(userId);
            if(noteModel.getNoteId() == null) noteService.addNote(noteModel);
            else noteService.updateNote(noteModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }


    @PostMapping(value = "/note/delete/{id}")
    public String deleteNote(Authentication authentication, @PathVariable(value = "id") Integer noteId){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            noteService.deleteNote(noteId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }
}
