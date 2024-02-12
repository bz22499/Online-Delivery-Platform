package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.services.UserService;
import com.sep.onlinedeliverysystem.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Optional;

@Controller
public class WebController {

    private final VendorService vendorService;
    private final UserService userService;

    @Autowired
    public WebController(VendorService vendorService, UserService userService) {
        this.vendorService = vendorService;
        this.userService = userService;
    }

    @GetMapping("")
    public String home(){ return "home"; }

    @GetMapping("/home")
    public String home2(){return "home";}


    @GetMapping("/vendor") //Read all from current user functionality
    public String getVendorPage(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("name", vendor.get().getName());
                model.addAttribute("description", vendor.get().getDescription());
                model.addAttribute("rating", vendor.get().getRating());
                return "vendor";
            } else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }


    @GetMapping("/about")
    public String about(){return "about-us";}

    @GetMapping("/contact")
    public String contact(){return "contactUs";}

    @GetMapping("/customerlogin")
    public String customerlogin(){ return "customerSignUp"; }

    @GetMapping("/driverlogin")
    public String driverlogin(){ return "driverSignUp"; }

    @GetMapping("/restaurantlogin")
    public String restaurant(){ return "restaurantSignUp"; }

    @GetMapping("/login")
    public String login() {return "login";}

    @GetMapping("/order")
    public String order(){
        return "order";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<User> user = userService.findOne(loggedInUserEmail);
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            if (user.isPresent()) {
                model.addAttribute("id", user.get().getEmail());
                model.addAttribute("firstName", user.get().getFirstName());
                model.addAttribute("lastName", user.get().getLastName());
                model.addAttribute("password", user.get().getPassword());
                return "profile";
            } else if(vendor.isPresent()){
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("firstName", vendor.get().getName());
                model.addAttribute("lastName", vendor.get().getName());
                model.addAttribute("password", vendor.get().getPassword());
                return "profile";
            }
            else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/{email}/menu-page")
    public String getMenuPage(@PathVariable("email") String email, Model model){
        Optional<Vendor> vendor = vendorService.findOne(email);
        if(vendor.isPresent()){
            model.addAttribute("id", vendor.get().getEmail());
            model.addAttribute("name", vendor.get().getName());
            model.addAttribute("description", vendor.get().getDescription());
            model.addAttribute("rating", vendor.get().getRating());
            return "menu-page";
        }
        else {
            return "notFound";
        }
    }

    @GetMapping("/vendoritems") //Read all from current user functionality
    public String getVendorItemsPage(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("name", vendor.get().getName());
                model.addAttribute("description", vendor.get().getDescription());
                model.addAttribute("rating", vendor.get().getRating());
                return "vendoritems";
            } else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

//    @GetMapping("/FILLER") //to get the current logged-in user/vendor
//    public ResponseEntity<Map<String, String>> getCurrentUserEmail(Principal principal) {
//        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
//
//        // Get the email from UserDetails
//        String vendorEmail = userDetails.getUsername();
//
//        Map<String, String> response = new HashMap<>();
//        response.put("email", vendorEmail);
//
//        return ResponseEntity.ok(response);
//    }
}
