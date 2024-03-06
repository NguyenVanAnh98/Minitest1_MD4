package com.example.md4_minitest1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CarController {
    @GetMapping ("/car")
    public String cars (){
        return "create";
    }
}