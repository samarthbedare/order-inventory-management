package com.project.orderinventorymanagement.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/hello")
    public String greet() {
        return "Hello";
    }
    
    @GetMapping("/hello1")
    public String greet1() {
        return "Hello123";
    }
}
