package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Messenger;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public RedirectView signupUser(@ModelAttribute("user") UserModel userModel, RedirectAttributes model) {
        String signupError = null;

        if (!userService.isUsernameAvailable(userModel.getUsername())) {
            signupError = Messenger.USER_EXISTS;
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(userModel);
            if (rowsAdded < 0) {
                signupError = Messenger.ERROR_DEFAULT;
            }
        }

        if (signupError == null) {
            model.addFlashAttribute("signupSuccess", true);
            RedirectView redirectView= new RedirectView("/login",true);
            return redirectView;

        } else {
            model.addFlashAttribute("signupError", signupError);
        }

        return new RedirectView("/signup",true);

    }
}
