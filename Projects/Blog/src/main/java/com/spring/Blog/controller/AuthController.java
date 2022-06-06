package com.spring.Blog.controller;

import com.spring.Blog.model.User;
import com.spring.Blog.service.AuthService;
import com.spring.Blog.utility.user.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping
    @RequestMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody User user, @RequestParam(name = "role", defaultValue = "USER") UserRoles role) {
        return authService.register(user, role);
    }

    @PostMapping
    @RequestMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping
    @RequestMapping(path = "/logout")
    public ResponseEntity<String> logout(@RequestBody User user) {
        return authService.logout(user);
    }
}
