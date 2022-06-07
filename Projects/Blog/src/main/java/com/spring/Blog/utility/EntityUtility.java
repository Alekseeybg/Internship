package com.spring.Blog.utility;

import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;

import com.spring.Blog.repository.BlogRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.user.ValidationMessages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.spring.Blog.utility.user.UserValidator.*;
import static com.spring.Blog.utility.user.UserRoles.*;


@Component
@AllArgsConstructor
public class EntityUtility {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;

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
        return user.getRole().equals(ADMIN.getRole());
    }

    public Blog getBlogById(long blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            return blog.get();
        } else {
            throw new ResourceNotFoundException("Blog not found");
        }
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
