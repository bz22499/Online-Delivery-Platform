package com.example.OnlineDeliveryPlatform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @GetMapping(value = "/home")
    public String index(){
        return "home";
    }
}
