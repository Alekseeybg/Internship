package com.spring.Blog.controller;

import com.spring.Blog.model.User;
import com.spring.Blog.service.AuthService;
import com.spring.Blog.utility.user.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        authService.register(user, role);
        if (role.equals(UserRoles.ADMIN)) {
            return new ResponseEntity<>("Admin registered successfully!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping(path = "/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User loggedUser = authService.login(user);
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(path = "/logout")
    public ResponseEntity<String> logout(@RequestBody User user) {
        authService.logout(user);
        return new ResponseEntity<>("User logged out", HttpStatus.NO_CONTENT);
    }
}