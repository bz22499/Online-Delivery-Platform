package com.sep.onlinedeliverysystem.controller;

import java.security.Principal;
import java.util.Optional;

import com.sep.onlinedeliverysystem.domain.entities.*;
import com.sep.onlinedeliverysystem.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final VendorService vendorService;
    private final DriverService driverService;
    private final UserService userService;
    private final UserAddressService userAddressService;
    private final VendorAddressService vendorAddressService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public WebController(VendorService vendorService, DriverService driverService, UserService userService,UserAddressService userAddressService ,VendorAddressService vendorAddressService ,@Qualifier("customAuthenticationManager") AuthenticationManager authenticationManager) {
        this.vendorService = vendorService;
        this.driverService = driverService;
        this.userService = userService;
        this.userAddressService = userAddressService;
        this.vendorAddressService = vendorAddressService;
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


    @GetMapping("/vendoradditems")
    public String getVendorPage(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName();
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("name", vendor.get().getName());
                model.addAttribute("description", vendor.get().getDescription());
                model.addAttribute("rating", vendor.get().getRating());
                return "vendorAddItems";
            } else {
                return "notFound";
            }
        } else {
            return "login";
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

    @GetMapping("/distanceCalculator")
    public String calculateDistance() {
        return "distance_calculator";
    }

    @GetMapping("/customerlogin")
    public String customerlogin() {
        return "customerSignUp";
    }

    @GetMapping("/driverlogin")
    public String driverlogin() {
        return "driverSignUp";
    }

    @GetMapping("/vendorsignup")
    public String restaurant() {
        return "vendorSignUp";
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

    @GetMapping("/ordersForDrivers")
    public String ordersForDrivers(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Driver> driver = driverService.findOne(loggedInUserEmail);

            if (driver.isPresent()) {
                model.addAttribute("id", driver.get().getEmail());
                return "ordersForDrivers";
            }else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/collectionForDriver")
    public String collectionForDriver(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Driver> driver = driverService.findOne(loggedInUserEmail);

            if (driver.isPresent()) {
                model.addAttribute("id", driver.get().getEmail());
                return "collectionForDriver";
            }else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/ordersForVendors")
    public String ordersForVendors(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                return "ordersForVendors";
            }else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }
    @GetMapping("/checkout")
    public String checkout(Principal principal, Model model){
        String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
        Optional<User> user = userService.findOne(loggedInUserEmail);
        if (user.isPresent()) {
            model.addAttribute("id", user.get().getEmail());
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

            if (user.isPresent()) {
                User userObj = user.get();
                model.addAttribute("id", user.get().getEmail());
                model.addAttribute("firstName", user.get().getFirstName());
                model.addAttribute("lastName", user.get().getLastName());
                model.addAttribute("password", user.get().getPassword());

                Optional<UserAddress> userAddress = userAddressService.findByUserEmail(userObj.getEmail());
                if(userAddress.isPresent()){
                    UserAddress userAddressObj = userAddress.get();
                    model.addAttribute("street", userAddressObj.getStreet());
                    model.addAttribute("city", userAddressObj.getCity());
                    model.addAttribute("country", userAddressObj.getCountry());
                    model.addAttribute("postcode", userAddressObj.getPostCode());
                }

                return "profile";
            }else {
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
                Vendor vendorObj = vendor.get();
                model.addAttribute("id", vendorObj.getEmail());
                model.addAttribute("name", vendorObj.getName());
                model.addAttribute("description", vendorObj.getDescription());
                model.addAttribute("rating", vendorObj.getRating());
                model.addAttribute("password", vendorObj.getPassword());
                model.addAttribute("imageURL",vendorObj.getImageUrl());

                Optional<VendorAddress> vendorAddress = vendorAddressService.findByVendorEmail(vendorObj.getEmail());
                if(vendorAddress.isPresent()){
                    VendorAddress vendorAddressObj = vendorAddress.get();
                    model.addAttribute("street", vendorAddressObj.getStreet());
                    model.addAttribute("city", vendorAddressObj.getCity());
                    model.addAttribute("country", vendorAddressObj.getCountry());
                    model.addAttribute("postcode", vendorAddressObj.getPostCode());
                }

                return "vendorProfile";
            } else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/driverProfile")
    public String driverProfile(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Driver> driver = driverService.findOne(loggedInUserEmail);
            if (driver.isPresent()) {
                model.addAttribute("id", driver.get().getEmail());
                model.addAttribute("name", driver.get().getName());
                model.addAttribute("rating", driver.get().getRating());
                model.addAttribute("password", driver.get().getPassword());
                return "driverProfile";
            } else {
                return "notFound";
            }
        } else {
            // Handle the case when no user is logged in
            return "login"; // Redirect to the login page
        }
    }

    @GetMapping("/{email}/menuPage")
    public String getMenuPage(@PathVariable("email") String email, Model model, Principal principal) {
        Optional<Vendor> vendor = vendorService.findOne(email);
        Optional<User> user = Optional.empty(); // Declare user variable outside the block

        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            user = userService.findOne(loggedInUserEmail);
        }
        if (vendor.isPresent()) {
            model.addAttribute("id", vendor.get().getEmail());
            model.addAttribute("name", vendor.get().getName());
            model.addAttribute("description", vendor.get().getDescription());
            model.addAttribute("rating", vendor.get().getRating());
            if (user.isPresent()){
                model.addAttribute("userId", user.get().getEmail());
            }
            return "menuPage";
        } else {
            return "notFound";
        }
    }

    @GetMapping("/vendoredititems") //Read all from current user functionality
    public String getVendorItemsPage(Principal principal, Model model) {
        if (principal != null) {
            String loggedInUserEmail = principal.getName(); // Retrieves the email/id of the currently logged-in user
            Optional<Vendor> vendor = vendorService.findOne(loggedInUserEmail);

            if (vendor.isPresent()) {
                model.addAttribute("id", vendor.get().getEmail());
                model.addAttribute("name", vendor.get().getName());
                model.addAttribute("description", vendor.get().getDescription());
                model.addAttribute("rating", vendor.get().getRating());
                return "vendoredititems";
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

    @GetMapping("/checkout-back")
    public String checkoutBack() {return "/${encodeURIComponent(restaurant.email)}/menu-page";}

    @GetMapping("/menu-page-back")
    public String menuBack() {return "order";}

    @GetMapping("/driver-sign-up")
    public String driverSignUp() {
        return "driverSignUp";
    }
}

