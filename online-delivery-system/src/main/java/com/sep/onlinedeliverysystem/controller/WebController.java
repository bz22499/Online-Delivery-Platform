package com.sep.onlinedeliverysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {
    @GetMapping("/home")
    public String home(){ return "home"; }

    @GetMapping("/login")
    @ResponseBody
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
