package com.spring.Blog.service;

import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.model.User;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityUtility entityUtility;

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            String message = ExceptionMessages.USERS_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        } else {
            return users;
        }
    }

    public List<User> getAdmins() {
        List<User> users = userRepository.findAll();
        users.removeIf(user -> !entityUtility.userIsAdmin(user));
        if (users.isEmpty()) {
            String message = ExceptionMessages.ADMINS_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        }
        return users;
    }

    public User getUserById(Long id) {
        String message = ExceptionMessages.USER_NOT_FOUND.getMessage();
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    public User getAdminById(Long id) {
        User user = entityUtility.getUserById(id);
        if (!entityUtility.userIsAdmin(user)) {
            String message = ExceptionMessages.ADMIN_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        }
        return user;
    }
}