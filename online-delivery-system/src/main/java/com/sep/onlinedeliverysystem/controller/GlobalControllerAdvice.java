package com.sep.onlinedeliverysystem.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    //All controllers can see the value of loggedIn
    @ModelAttribute
    public void globalAttributes(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("loggedIn", true); // User is logged in
        } else {
            model.addAttribute("loggedIn", false); // User is not logged in
        }
    }
}