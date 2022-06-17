package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.EntityUtility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import org.springframework.boot.test.context.SpringBootTest;

import static com.spring.Blog.utility.user.UserRoles.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.spring.Blog.utility.exception.ExceptionMessages.*;
import static com.spring.Blog.utility.user.ValidationMessages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EntityUtility entityUtility;
    private User user;
    private User admin;
    private List<User> users;
    private List<User> admins;
    private List<User> emptyList;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createFakeUsers();
    }

    @Test
    public void givenNoUsersInRepositoryWhenLookingForUsersThrowException() {
        when(userRepository.findAll()).thenReturn(emptyList);
        try {
            userService.getUsers();
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), USERS_NOT_FOUND.getMessage());
        }
    }

    @Test
    public void givenUsersInRepositoryWhenLookingForUsersReturnUsers() {
        List<User> users1 = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        try {
            users1 = userService.getUsers();
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), USERS_NOT_FOUND.getMessage());
        }
        assertEquals(users, users1);
    }

    @Test
    public void givenNoAdminsInRepositoryWhenLookingForAdminsThrowException() {
        when(userRepository.findAll()).thenReturn(users);
        when(admins.removeIf(admin -> !entityUtility.userIsAdmin(admin))).thenReturn(true);
        try {
            userService.getAdmins();
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ADMINS_NOT_FOUND.getMessage());
        }
    }

    @Test
    public void givenWrongUserIdWhenLookingForUserThrowException() {
        doReturn(Optional.empty()).when(userRepository).findById(1L);
        try {
            userService.getUserById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), USER_NOT_FOUND.getMessage());
        }
    }

    @Test
    public void givenCorrectUserIdWhenLookingForUserReturnUser() {
        User user1;
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        user1 = userService.getUserById(1L);

        assertEquals(user, user1);
    }

    @Test
    public void givenWrongAdminIdWhenLookingForAdminThrowException() {
        when(entityUtility.getUserById(1L)).thenReturn(user);
        try {
            userService.getAdminById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ADMIN_NOT_FOUND.getMessage());
        }
    }

    @Test
    public void givenCorrectAdminIdWhenLookingForAdminReturnAdmin() {
        User admin1;
        when(entityUtility.getUserById(1L)).thenReturn(admin);
        when(entityUtility.userIsAdmin(admin)).thenReturn(true);

        admin1 = userService.getAdminById(1L);

        assertEquals(admin, admin1);
    }


    private void createFakeUsers() {
        user = new User("User", "user@mail.com", "p@SSw0rd", USER.getRole());
        admin = new User("Admin", "admin@gmail.com", "p@SSw0rd", ADMIN.getRole());

        users = new ArrayList<>();
        users.add(user);

        admins = new ArrayList<>();
        admins.add(admin);

        emptyList = new ArrayList<>();
    }
}