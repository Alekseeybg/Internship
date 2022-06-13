package com.spring.Blog.service;

import com.spring.Blog.model.User;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.user.UserRoles;
import com.sun.xml.bind.v2.TODO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static com.spring.Blog.utility.exception.ExceptionMessages.USERNAME_EMAIL_EXISTS;
import static com.spring.Blog.utility.user.ValidationMessages.*;
import static org.junit.Assert.assertEquals;
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
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User("User", "user@mail.com", "p@SSw0rd");
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }


    private void validateUser(User user) {
        when(entityUtility.validateUser(user)).thenReturn(NAME_IS_NOT_VALID);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), NAME_IS_NOT_VALID.getMessage());
        }
    }

    private void validatePassword(User user) {
        when(entityUtility.validateUser(user)).thenReturn(PASSWORD_IS_NOT_VALID);
        try {
            authService.register(user, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), PASSWORD_IS_NOT_VALID.getMessage());
        }
    }
    //@Test(expected = NullPointerException.class)
    //public void givenNullUsernameThenReturnValidationMessage() {
    //   user.setUsername(null);
    // }

    @Test
    public void givenEmptyUsernameThenReturnValidationMessage() {
        user.setUsername("");
        validateUser(user);
    }

    @Test
    public void givenUsernameWithForbiddenSymbolsThenReturnValidationMessage() {
        user.setUsername("Adf@S");
        validateUser(user);
    }

    @Test
    public void givenUsernameTooShortSymbolsThenReturnValidationMessage() {
        user.setUsername("ad");
        validateUser(user);
    }

    @Test
    public void givenPasswordTooShortWhenRegisterThenReturnValidationMessage() {
        user.setPassword("");
        validatePassword(user);
    }

    @Test
    public void givenTooShortPasswordWhenRegisterThenReturnValidationMessage() {
        user.setPassword("123");
        validatePassword(user);
    }

    @Test
    public void givenPasswordWihtNoDigitsWhenRegisterThenReturnValidationMessage() {
        user.setPassword("P@assWord");
        validatePassword(user);
    }

    @Test
    public void givenPasswordWihtNoUpperCaseLetterWhenRegisterThenReturnValidationMessage() {
        user.setPassword("p@assW0rd");
        validatePassword(user);
    }

    @Test
    public void givenPasswordWihtNoLowerCaseLetterWhenRegisterThenReturnValidationMessage() {
        user.setPassword("P@ASSW0RD");
        validatePassword(user);
    }

    @Test
    public void givenPasswordWihtNoSpecialSymbolWhenRegisterThenReturnValidationMessage() {
        user.setPassword("Passw0rd");
        validatePassword(user);
    }

    @Test(expected = UnprocessableEntityException.class)
    public void givenEmailExistsWhenRegisterThenThrowException() {
        //  TODO: fix this test
        User user1 = new User("User1", "user@mail.com", "p@SSw0rd");
        when(entityUtility.emailExists(user1.getEmail())).thenThrow(new UnprocessableEntityException(USERNAME_EMAIL_EXISTS.getMessage()));
        try {
            authService.register(user1, UserRoles.USER);
        } catch (UnprocessableEntityException e) {
            assertEquals(e.getMessage(), USERNAME_EMAIL_EXISTS.getMessage());
        }
    }
}
