package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.services.UserService;
import com.sep.onlinedeliverysystem.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;
    private final VendorService vendorService;

    @Autowired
    public GlobalControllerAdvice(UserService userService, VendorService vendorService) {
        this.userService = userService;
        this.vendorService = vendorService;
    }

    // All controllers can see the value of loggedIn, loggedInUser, and loggedInVendor
    @ModelAttribute
    public void globalAttributes(Principal principal, Model model) {
        boolean loggedIn = (principal != null);
        model.addAttribute("loggedIn", loggedIn); // User is logged in or not

        boolean loggedInUser = false;
        boolean loggedInVendor = false;

        if (loggedIn) {
            String loggedInUserEmail = principal.getName();
            Optional<User> user = userService.findOne(loggedInUserEmail);
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            loggedInUser = user.isPresent();
            loggedInVendor = vendor.isPresent();
        }

        model.addAttribute("loggedInUser", loggedInUser); // logged in and is a user
        model.addAttribute("loggedInVendor", loggedInVendor); // logged in and is a vendor
    }
}