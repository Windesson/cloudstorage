package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class NoteController {

    NoteService noteService;
    UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(value = "/note")
    public RedirectView newNote(Authentication authentication, NoteModel noteModel, RedirectAttributes model){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            noteModel.setUserId(userId);
            if(noteModel.getNoteTitle().isEmpty() || noteModel.getNoteDescription().isEmpty()){
                model.addFlashAttribute("error", "Please fill in all the required fields.");
            }
            else if(noteModel.getNoteId() == null) {
                if(noteService.noteExists(noteModel.getNoteTitle(), userId)){
                    model.addFlashAttribute("error", "A note with the title already exists.");
                } else {
                    noteService.addNote(noteModel);
                }
            }
            else {
                noteService.updateNote(noteModel);
            }
        } catch (Exception e) {
            model.addFlashAttribute("error", "An error occurred please try again later.");
        }

        RedirectView redirectView= new RedirectView("/home",true);
        return redirectView;
    }


    @PostMapping(value = "/note/delete/{id}")
    public RedirectView deleteNote(Authentication authentication, @PathVariable(value = "id") Integer noteId, RedirectAttributes model){
        Integer userId = userService.getUserId(authentication.getName());

        try {
            noteService.deleteNote(noteId, userId);
        } catch (Exception e) {
            model.addFlashAttribute("error", "An error occurred please try again later.");
        }

        RedirectView redirectView= new RedirectView("/home",true);
        return redirectView;
    }
}
