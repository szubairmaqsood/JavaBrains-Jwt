package com.example.Java.Brains.Jwt.Controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeResource {
    /* Following url return Home Resource  */
    @GetMapping("/home")
    fun home():String{
        return "Home";
    }
}