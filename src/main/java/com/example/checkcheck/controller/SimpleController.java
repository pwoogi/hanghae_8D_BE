package com.example.checkcheck.controller;

import com.example.checkcheck.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/")
    public String simpleCon() {

        return "테스트테스트테스트";

    }
    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }

}
