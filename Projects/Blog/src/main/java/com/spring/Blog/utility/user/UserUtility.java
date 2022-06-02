package com.spring.Blog.utility.user;

import com.spring.Blog.model.User;

import com.spring.Blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import static com.spring.Blog.utility.user.UserValidator.*;

@Component
public class UserUtility {


    private static UserRepository userRepository;


    @Autowired
    public UserUtility(UserRepository userRepository) {
        UserUtility.userRepository = userRepository;
    }


    public static ValidationMessages validateUser(User user) {
        return (isValidName())
                .and(isValidEmail())
                .and(isValidPassword())
                .apply(user);
    }


    public static boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public static boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public static boolean correctPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

    public static boolean userLogged(User user) {
        return user.isLogged();
    }
}
