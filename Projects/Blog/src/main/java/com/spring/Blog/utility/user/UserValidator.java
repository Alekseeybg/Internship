package com.spring.Blog.utility.user;

import com.spring.Blog.model.User;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.spring.Blog.utility.user.ValidationMessages.*;
import static com.spring.Blog.utility.user.ValidationMessages.SUCCESS;

public interface UserValidator extends Function<User, ValidationMessages> {

    static UserValidator isValidName() {
        return user -> {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]{3,100}$");
            Matcher matcher = pattern.matcher(user.getUsername());
            return matcher.matches() ? SUCCESS : NAME_IS_NOT_VALID;
        };
    }

    static UserValidator isValidEmail() {
        return user -> {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]{3,255}$");
            Matcher matcher = pattern.matcher(user.getEmail());
            return matcher.matches() ? SUCCESS : EMAIL_IS_NOT_VALID;
        };
    }

    static UserValidator isValidPassword() {
        return user -> {
            Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,72}$");
            Matcher matcher = pattern.matcher(user.getPassword());
            return matcher.matches() ? SUCCESS : PASSWORD_IS_NOT_VALID;
        };
    }

    default UserValidator and(UserValidator other) {
        return user -> {
            ValidationMessages result = this.apply(user);
            return result.equals(SUCCESS) ? other.apply(user) : result;
        };
    }
}