package com.sh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {


    @Value("${app.version}")
    private String version;
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("version", version);
        return "index";
    }
}