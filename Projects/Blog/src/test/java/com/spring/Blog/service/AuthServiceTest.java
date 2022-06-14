package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.user.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.spring.Blog.utility.exception.ExceptionMessages.*;
import static com.spring.Blog.utility.user.ValidationMessages.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EntityUtility entityUtility;
    private User user;
    private User userLogin;
    private String result;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("User", "user@mail.com", "p@SSw0rd");
        userLogin = new User("user@mail.com","p@SSw0rd");
        result = null;
    }

    @Test
    public void givenNullUsernameThenReturnValidationMessage() {
        user.setUsername(null);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, NAME_IS_NULL.getMessage());
    }

    @Test
    public void givenInvalidUsernameThenReturnValidationMessage() {
        user.setUsername("ad");
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, NAME_IS_NOT_VALID.getMessage());
    }

    @Test
    public void givenNullPasswordThenReturnValidationMessage() {
        user.setPassword(null);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, PASSWORD_IS_NULL.getMessage());
    }

    @Test
    public void givenInvalidPasswordWhenRegisterThenReturnValidationMessage() {
        user.setPassword("");
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, PASSWORD_IS_NOT_VALID.getMessage());
    }

    @Test
    public void givenNullEmailThenReturnValidationMessage() {
        user.setEmail(null);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, EMAIL_IS_NULL.getMessage());
    }

    @Test
    public void givenInvalidEmailThenReturnValidationMessage() {
        user.setEmail("emailgmail.com");
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, EMAIL_IS_NOT_VALID.getMessage());
    }

    @Test
    public void givenUsernameExistsWhenRegisterThenThrowException() {
        User user1 = new User("User", "user2@mail.com", "p@SSw0rd");
        try {
            authService.register(user1, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, USERNAME_EMAIL_EXISTS.getMessage());
    }

    @Test
    public void givenEmailExistsWhenRegisterThenThrowException() {
        User user1 = new User("User2", "user@mail.com", "p@SSw0rd");
        try {
            authService.register(user1, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            result = e.getMessage();
        }
        assertEquals(result, USERNAME_EMAIL_EXISTS.getMessage());
    }

    //Login
    @Test
    public void givenWrongEmailWhenLoginThenReturnValidationMessage() {
        user.setEmail("");
        when(authService.login(user)).thenThrow(ResourceNotFoundException.class);

        try {
            authService.login(user);
        } catch (ResourceNotFoundException e) {
            result = e.getMessage();
        }
        assertEquals(result, USER_NOT_FOUND.getMessage());
    }
}