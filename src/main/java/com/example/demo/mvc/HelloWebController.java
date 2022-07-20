package com.example.demo.mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWebController {
    @GetMapping()
    String msg() {
        return "hi";
    }
}
