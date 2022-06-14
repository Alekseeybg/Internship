package com.spring.Blog.utility.user;

import com.spring.Blog.model.User;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.spring.Blog.utility.user.ValidationMessages.*;

public interface UserValidator extends Function<User, ValidationMessages> {

    static UserValidator usernameIsNotNull() {
        return user -> user.getUsername() != null ? SUCCESS : NAME_IS_NULL;
    }

    static UserValidator isValidName() {
        return user -> {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]{3,100}$");

            Matcher matcher = pattern.matcher(user.getUsername());
            return matcher.matches() ? SUCCESS : NAME_IS_NOT_VALID;
        };
    }

    static UserValidator isValidEmail() {
        return user -> {
            Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
            if (user.getEmail() == null) {
                return EMAIL_IS_NULL;
            }
            Matcher matcher = pattern.matcher(user.getEmail());
            return matcher.matches() ? SUCCESS : EMAIL_IS_NOT_VALID;
        };
    }

    static UserValidator isValidPassword() {
        return user -> {
            Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,72}$");

            if (user.getPassword() == null) {
                return PASSWORD_IS_NULL;
            }
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