package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.user.UserRoles;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.user.ValidationMessages;
import com.spring.Blog.utility.exception.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static com.spring.Blog.utility.user.UserValidator.*;
import static com.spring.Blog.utility.user.UserValidator.isValidPassword;
import static com.spring.Blog.utility.user.ValidationMessages.SUCCESS;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityUtility entityUtility;

    public User register(@RequestBody User user, UserRoles role) {
        ValidationMessages result = validateUser(user);
        if (result != SUCCESS) {
            throw new UnprocessableEntityException(result.getMessage());
        } else if (entityUtility.userExists(user.getUsername()) || entityUtility.emailExists(user.getEmail())) {
            String message = ExceptionMessages.USERNAME_EMAIL_EXISTS.getMessage();
            throw new UnprocessableEntityException(message);
        }
        user.setRole(role.getRole());
        return userRepository.save(user);
    }

    public User login(@RequestBody User user) {
        User userDb = entityUtility.getUserByEmail(user.getEmail());
        if (!entityUtility.correctPassword(userDb, user.getPassword())) {
            String message = ExceptionMessages.INCORRECT_PASSWORD.getMessage();
            throw new UnauthorizedException(message);
        }
        userDb.setLogged(true);
        return userRepository.save(userDb);
    }

    public void logout(@RequestBody User user) {
        User userDb = entityUtility.getUserByEmail(user.getEmail());
        if (!entityUtility.userLogged(userDb)) {
            String message = ExceptionMessages.USER_NOT_LOGGED.getMessage();
            throw new UnauthorizedException(message);
        }
        userDb.setLogged(false);
        userRepository.save(userDb);
    }

    private ValidationMessages validateUser(User user) {
        return (usernameIsNotNull())
                .and(isValidName())
                .and(isValidEmail())
                .and(isValidPassword())
                .apply(user);
    }
}