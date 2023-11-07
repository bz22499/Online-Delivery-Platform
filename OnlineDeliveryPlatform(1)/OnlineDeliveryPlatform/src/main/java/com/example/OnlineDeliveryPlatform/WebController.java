package com.example.OnlineDeliveryPlatform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @GetMapping(value = "/home")
    public String home(){
        return "home";
    }

    @GetMapping(value = "/login")
    @ResponseBody
    public String login(){
        return "login";
    }
    @GetMapping(value = "/order")
    @ResponseBody
    public String order(){
        return "order";
    }

    @GetMapping(value = "/partner")
    @ResponseBody
    public String partner(){
        return "partner";
    }

    @GetMapping(value = "/signup")
    @ResponseBody
    public String signup(){
        return "signup";
    }

}


