package com.spring.Blog.controller;

import com.spring.Blog.service.UserService;
import com.spring.Blog.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @RequestMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(path = "/admins")
    public ResponseEntity<List<User>> getAdmins() {
        return new ResponseEntity<>(userService.getAdmins(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/admins/{id}")
    public ResponseEntity<User> getAdminById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getAdminById(id), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }
}
