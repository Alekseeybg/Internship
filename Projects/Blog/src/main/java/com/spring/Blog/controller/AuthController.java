package com.spring.Blog.controller;

import com.spring.Blog.model.User;
import com.spring.Blog.service.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth/")
@Getter
@Setter
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping
    @RequestMapping(path = "register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping
    @RequestMapping(path = "login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping
    @RequestMapping(path = "logout")
    public ResponseEntity<User> logout(@RequestBody User user) {
        return authService.logout(user);
    }
}
