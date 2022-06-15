package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.user.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static com.spring.Blog.utility.exception.ExceptionMessages.*;
import static com.spring.Blog.utility.user.ValidationMessages.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("User", "user@mail.com", "p@SSw0rd");
        userLogin = new User("user@mail.com", "p@SSw0rd");
    }

    @Test
    public void givenNullUsernameThenReturnValidationMessage() {
        user.setUsername(null);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), NAME_IS_NULL.getMessage());
        }
        verify(userRepository, never()).save(user);
    }

    @Test
    public void givenInvalidUsernameThenReturnValidationMessage() {
        user.setUsername("ad");
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), NAME_IS_NOT_VALID.getMessage());
        }
        verify(userRepository, never()).save(user);
    }

    @Test
    public void givenNullPasswordThenReturnValidationMessage() {
        user.setPassword(null);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), PASSWORD_IS_NULL.getMessage());
        }
        verify(userRepository, never()).save(user);
    }

    @Test
    public void givenInvalidPasswordWhenRegisterThenReturnValidationMessage() {
        user.setPassword("");
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), PASSWORD_IS_NOT_VALID.getMessage());
        }
        verify(userRepository, never()).save(user);
    }

    @Test
    public void givenNullEmailThenReturnValidationMessage() {
        user.setEmail(null);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), EMAIL_IS_NULL.getMessage());
        }
        verify(userRepository, never()).save(user);
    }

    @Test
    public void givenInvalidEmailThenReturnValidationMessage() {
        user.setEmail("emailgmail.com");
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), EMAIL_IS_NOT_VALID.getMessage());
        }
        verify(userRepository, never()).save(user);
    }

    @Test
    public void givenUsernameExistsWhenRegisterThenThrowException() {
        User user1 = new User("User", "user2@mail.com", "p@SSw0rd");
        when(entityUtility.userExists("User")).thenReturn(true);
        try {
            authService.register(user1, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), USERNAME_EMAIL_EXISTS.getMessage());
        }
        verify(userRepository, never()).save(user1);
    }

    @Test
    public void givenEmailExistsWhenRegisterThenThrowException() {
        User user1 = new User("User2", "user@mail.com", "p@SSw0rd");
        when(entityUtility.emailExists("user@mail.com")).thenReturn(true);
        try {
            authService.register(user1, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), USERNAME_EMAIL_EXISTS.getMessage());
        }
        verify(userRepository, never()).save(user1);
    }
    @Test
    public void givenValidUserWhenLoginThenReturnUser() {
        when(entityUtility.userExists("User")).thenReturn(false);
        when(entityUtility.emailExists("user@mail.com")).thenReturn(false);
        authService.register(user, UserRoles.USER);
        verify(userRepository, times(1)).save(user);
    }

    //Login
    @Test
    public void givenWrongEmailWhenLoginThenThrowException() {
        userLogin.setEmail("user232@mail.com");
        when(entityUtility.getUserByEmail(userLogin.getEmail())).thenReturn(user);
        when(entityUtility.correctPassword(user, userLogin.getPassword())).thenReturn(true);
        try {
            authService.login(userLogin);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), USER_NOT_FOUND.getMessage());
        }
        assertFalse(userLogin.isLogged());
        verify(userRepository, never()).save(userLogin);
    }

    @Test
    public void givenWrongPasswordWhenLoginThenThrowException() {
        //userLogin.setPassword("p@SSw0rd");
        userLogin.setPassword("wrongpassword");
        when(entityUtility.getUserByEmail(userLogin.getEmail())).thenReturn(userLogin);
        when(entityUtility.correctPassword(userLogin, user.getPassword())).thenReturn(false);
        assertNotEquals(userLogin.getPassword(), user.getPassword());
        try {
            authService.login(userLogin);
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), INCORRECT_PASSWORD.getMessage());
        }
        assertFalse(userLogin.isLogged());
        verify(userRepository, never()).save(userLogin);
    }

    @Test
    public void givenCorrectPasswordWhenLoginThenReturnUser() {
        userLogin.setPassword("p@SSw0rd");
        when(entityUtility.getUserByEmail(userLogin.getEmail())).thenReturn(userLogin);
        when(entityUtility.correctPassword(userLogin, user.getPassword())).thenReturn(true);
        assertEquals(userLogin.getPassword(), user.getPassword());
        authService.login(userLogin);
        assertTrue(userLogin.isLogged());
        verify(userRepository, times(1)).save(userLogin);
    }
   //logout
    @Test
    public void givenUserIsNotLoggedWhenLogoutThenThrowException() {
        userLogin.setLogged(false);
        when(entityUtility.userLogged(userLogin)).thenReturn(false);
        try {
            authService.logout(userLogin);
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), USER_NOT_LOGGED.getMessage());
        }
        assertFalse(userLogin.isLogged());
        verify(userRepository, never()).save(userLogin);
    }
    @Test
    public void givenUserIsLoggedWhenLogoutThenLogoutUser() {
        when(entityUtility.getUserByEmail(userLogin.getEmail())).thenReturn(userLogin);
        when(entityUtility.userLogged(userLogin)).thenReturn(true);
        try{
            authService.logout(userLogin);
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), USER_NOT_LOGGED.getMessage());
        }
        assertFalse(userLogin.isLogged());
        verify(userRepository, times(1)).save(userLogin);
    }
}