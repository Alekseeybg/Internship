package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.user.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.spring.Blog.utility.exception.ResourceNotFoundException;

import static com.spring.Blog.utility.user.UserRoles.ADMIN;
import static com.spring.Blog.utility.user.UserRoles.USER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.spring.Blog.utility.exception.ExceptionMessages.*;
import static com.spring.Blog.utility.user.ValidationMessages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void givenNoAdminsInRepositoryWhenLookingForAdminsThrowException() {
        when(userRepository.findAll()).thenReturn(users);
        when(admins.removeIf(admin -> !entityUtility.userIsAdmin(admin))).thenReturn(true);
        try {
            userService.getAdmins();
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ADMINS_NOT_FOUND.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> userService.getAdmins());
    }

    @Test
    public void givenUserIdWhenLookingForUserThrowException() {
        doReturn(Optional.empty()).when(userRepository).findById(1L);
        //User userByService = userService.getUserById(1L);
        //when(userRepository.findById(anyLong()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND.getMessage()))).thenReturn(user);

        try {
            userService.getUserById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), USER_NOT_FOUND.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void givenAdminIdWhenLookingForAdminThrowException() {
        //doReturn(user).when(userRepository).findById(1L);
        when(entityUtility.getUserById(1L)).thenReturn(user);
        try {
            userService.getAdminById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ADMIN_NOT_FOUND.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> userService.getAdminById(1L));
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