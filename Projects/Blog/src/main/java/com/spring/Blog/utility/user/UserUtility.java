package com.spring.Blog.utility.user;

import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;

import com.spring.Blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.spring.Blog.utility.user.UserValidator.*;
import static com.spring.Blog.utility.user.UserRoles.*;


@Component
@AllArgsConstructor
public class UserUtility {

    @Autowired
    private UserRepository userRepository;

    public ValidationMessages validateUser(User user) {
        return (isValidName())
                .and(isValidEmail())
                .and(isValidPassword())
                .apply(user);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean correctPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

    public boolean userLogged(User user) {
        return user.isLogged();
    }

    public boolean userIsOwner(Blog blog, User user) {
        return blog.getOwner().equals(user);
    }

    public boolean userIsAdmin(User user) {

        return user.getRole().equals(ADMIN);
    }
}
