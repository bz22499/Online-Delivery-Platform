package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.services.UserService;
import com.sep.onlinedeliverysystem.services.VendorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class WebController {

    private final VendorService vendorService;
    private final UserService userService;

    private AuthenticationManager authenticationManager;

    @Autowired
    public WebController(VendorService vendorService, UserService userService, @Qualifier("customAuthenticationManager") AuthenticationManager authenticationManager) {
        this.vendorService = vendorService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("")
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    public String home2() {
        return "home";
    }


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
    public String about() {
        return "about-us";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contactUs";
    }

    @GetMapping("/customerlogin")
    public String customerlogin() {
        return "customerSignUp";
    }

    @GetMapping("/driverlogin")
    public String driverlogin() {
        return "driverSignUp";
    }

    @GetMapping("/restaurantlogin")
    public String restaurant() {
        return "restaurantSignUp";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/order")
    public String order() {
        return "order";
    }

    @GetMapping("/driverMain")
    public String driverMain(){return "driverMain"; }

    @GetMapping("/checkout")
    public String checkout(Principal principal){
        if (principal != null) {
            return "checkout";
        }
        else{
            return "login";
        }
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
            } else if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("firstName", vendor.get().getName());
                model.addAttribute("lastName", vendor.get().getName());
                model.addAttribute("password", vendor.get().getPassword());
                return "profile";
            } else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/vendorProfile")
    public String vendorProfile(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);
            if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("name", vendor.get().getName());
                model.addAttribute("description", vendor.get().getDescription());
                model.addAttribute("rating", vendor.get().getRating());
                model.addAttribute("password", vendor.get().getPassword());
                return "vendorProfile";
            } else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/{email}/menu-page")
    public String getMenuPage(@PathVariable("email") String email, Model model) {
        Optional<Vendor> vendor = vendorService.findOne(email);
        if (vendor.isPresent()) {
            model.addAttribute("id", vendor.get().getEmail());
            model.addAttribute("name", vendor.get().getName());
            model.addAttribute("description", vendor.get().getDescription());
            model.addAttribute("rating", vendor.get().getRating());
            return "menu-page";
        } else {
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

    @PostMapping("/login") //handle login after registration
    public String loginSubmit(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            // Perform authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Redirect to the appropriate page based on user role
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("VENDOR"))) {
                return "redirect:/vendor";
            } else {
                return "redirect:/home";
            }
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/baskets-overview")
    public String getBasketsPage() {
        return "baskets-overview";
    }
}
