package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.Blog.BlogApplication;
import com.spring.Blog.model.User;
import com.spring.Blog.service.AuthService;
import com.spring.Blog.utility.user.UserRoles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.spring.Blog.utility.user.UserRoles.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class AuthControllerTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1/auth";
    private static final String USERNAME = "username";
    private static final UserRoles FAKE_ROLE = USER;
    private static final UserRoles FAKE_ADMIN_ROLE = ADMIN;
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void whenRegisterUserThenReturn201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/register")
                        .content(mapper.writeValueAsString(userBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(authService, times(1)).register(userCaptor.capture(), eq(FAKE_ROLE));
    }

    @Test
    public void whenRegisterAdminThenReturn201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/register?role="+FAKE_ADMIN_ROLE)
                        .content(mapper.writeValueAsString(userBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(authService, times(1)).register(userCaptor.capture(), eq(FAKE_ADMIN_ROLE));
    }

    @Test
    public void whenLoginUserThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/login")
                        .content(mapper.writeValueAsString(userBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(authService, times(1)).login(userCaptor.capture());
    }

  @Test
    public void whenLogoutUserThenReturn204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/logout")
                        .content(mapper.writeValueAsString(userBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(authService, times(1)).logout(userCaptor.capture());
    }


    private User userBuilder() {
        User user = new User();
        user.setUsername("username");
        user.setEmail("user@mail.com");
        user.setPassword("p@ssw0rd");
        user.setRole(USER.getRole());
        return user;
    }
}
