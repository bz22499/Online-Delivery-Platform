package com.sep.onlinedeliverysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @GetMapping("/home")
    public String home(){ return "home"; }

    @GetMapping("/vendor")
    public String vendor(){
        return "vendor";
    }


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

    @GetMapping("/partner")
    @ResponseBody
    public String partner(){
        return "partner";
    }

    @GetMapping("/signup")
    @ResponseBody
    public String signup(){
        return "signup";
    }
}
