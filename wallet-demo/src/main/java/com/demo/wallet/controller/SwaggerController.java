package com.demo.wallet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    @GetMapping("/")
    public String getSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
