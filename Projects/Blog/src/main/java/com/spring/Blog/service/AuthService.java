package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.AuthRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.user.UserUtility;
import com.spring.Blog.utility.user.ValidationMessages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static com.spring.Blog.utility.user.UserValidator.*;
import static com.spring.Blog.utility.user.ValidationMessages.SUCCESS;



@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> register(@RequestBody User user) {
        ValidationMessages result = UserUtility.validateUser(user);
        if (result != SUCCESS) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        if (UserUtility.userExists(user.getUsername()) || UserUtility.emailExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("Username or Email already exists");
        }
        try {
            authRepository.save(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    public ResponseEntity<String> login(@RequestBody User user) {
        if (!UserUtility.emailExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("User not found");
        } else {
            User userDb = userRepository.findByEmail(user.getEmail());
            if (!UserUtility.correctPassword(userDb, user.getPassword())) {
                return ResponseEntity.badRequest().body("Incorrect password");
            } else {
                userDb.setLogged(true);
                userRepository.save(userDb);
                return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<User> logout(@RequestBody User user) {
        if (UserUtility.emailExists(user.getEmail())) {
            User userDb = authRepository.findUserByEmail(user.getEmail());
            if (UserUtility.userLogged(userDb)) {
                userDb.setLogged(false);
                userRepository.save(userDb);
                return new ResponseEntity<>(userDb, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}