package com.lab1.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/one")
    public String demoHello() {
        return "Hello World!";
    }

    @GetMapping("/two")
    @PreAuthorize("hasRole('ADMIN')")
    public String demoVlad() {
        return "Piba is the best!)";
    }
}
