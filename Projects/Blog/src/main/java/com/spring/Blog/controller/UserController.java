package com.spring.Blog.controller;

import com.spring.Blog.service.UserService;
import com.spring.Blog.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return userService.getUsers();
    }

/*    @GetMapping(path = "api/v1/admins")
    public ResponseEntity<List<User>> getAdmins() {
        return userService.getAdmins();
    }

    @GetMapping
    @RequestMapping(value = "api/v1/admins/{id}")
    public ResponseEntity<User> getAdminById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }*/

    @GetMapping
    @RequestMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }
}
