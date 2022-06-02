package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.user.UserRoles;
import com.spring.Blog.utility.user.UserUtility;
import com.spring.Blog.utility.user.ValidationMessages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.spring.Blog.utility.user.ValidationMessages.SUCCESS;

@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private UserRoles role;

    public ResponseEntity<String> register(@RequestBody User user, @RequestParam(name = "role", defaultValue = "UserRoles.USER") UserRoles role) {
        ValidationMessages result = userUtility.validateUser(user);
        if (result != SUCCESS) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        if (userUtility.userExists(user.getUsername()) || userUtility.emailExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("Username or Email already exists");
        }
        try {
            userRepository.save(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> login(@RequestBody User user) {
        if (!userUtility.emailExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("User not found");
        } else {
            User userDb = userRepository.findByEmail(user.getEmail());
            if (!userUtility.correctPassword(userDb, user.getPassword())) {
                return ResponseEntity.badRequest().body("Incorrect password");
            } else {
                userDb.setLogged(true);
                userRepository.save(userDb);
                return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<String> logout(@RequestBody User user) {
        if (userUtility.emailExists(user.getEmail())) {
            User userDb = userRepository.findByEmail(user.getEmail());
            if (userUtility.userLogged(userDb)) {
                userDb.setLogged(false);
                userRepository.save(userDb);
                return new ResponseEntity<>("Deleted", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}