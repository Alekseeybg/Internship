package com.spring.Blog.service;

import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.model.User;
import com.spring.Blog.utility.user.UserUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.spring.Blog.utility.user.UserRoles.*;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtility userUtility;

    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getAdmins() {
        List<User> users = userRepository.findAll();
        users.removeIf(user -> !userUtility.userIsAdmin(user));
        return users.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<User> getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<User> getAdminById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            if (userUtility.userIsAdmin(user)) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}