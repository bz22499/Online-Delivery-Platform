package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
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

    @Autowired
    public WebController(VendorService vendorService) {
        this.vendorService = vendorService;
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
    public String login(){
        return "login";
    }
    @GetMapping("/order")
    public String order(){
        return "order";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
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
