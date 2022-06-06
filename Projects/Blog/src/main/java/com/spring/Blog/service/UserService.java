package com.spring.Blog.service;

import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.model.User;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.user.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtility userUtility;

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        } else {
            return users;
        }
    }

    public ResponseEntity<List<User>> getAdmins() {
        List<User> users = userRepository.findAll();
        users.removeIf(user -> !userUtility.userIsAdmin(user));
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No admins found");
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + " not found"));
    }

    public User getAdminById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            if (userUtility.userIsAdmin(user)) {
                return user;
            }
        }
        return null;
    }
}