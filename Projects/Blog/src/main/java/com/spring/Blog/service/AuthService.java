package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.user.UserRoles;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.user.ValidationMessages;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static com.spring.Blog.utility.user.ValidationMessages.SUCCESS;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityUtility entityUtility;

    public User register(User user, UserRoles role) {
        ValidationMessages result = entityUtility.validateUser(user);
        if (result != SUCCESS) {
            throw new UnprocessableEntityException(result.getMessage());
        } else if (entityUtility.userExists(user.getUsername()) || entityUtility.emailExists(user.getEmail())) {
            throw new UnprocessableEntityException("Username or Email already exists");
        }
        user.setRole(role.getRole());
        return userRepository.save(user);
    }

    public User login(@RequestBody User user) {
        if (!entityUtility.emailExists(user.getEmail())) {
            throw new ResourceNotFoundException("Email not found");
        } else {
            User userDb = userRepository.findByEmail(user.getEmail());
            if (!entityUtility.correctPassword(userDb, user.getPassword())) {
                throw new UnauthorizedException("Incorrect password");
            } else {
                userDb.setLogged(true);
                return userRepository.save(userDb);
            }
        }
    }

    public ResponseEntity<String> logout(@RequestBody User user) {
        if (entityUtility.emailExists(user.getEmail())) {
            User userDb = userRepository.findByEmail(user.getEmail());
            if (entityUtility.userLogged(userDb)) {
                userDb.setLogged(false);
                userRepository.save(userDb);
                return new ResponseEntity<>("User logged out", HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}