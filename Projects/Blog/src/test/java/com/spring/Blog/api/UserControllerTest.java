package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.Blog.BlogApplication;
import com.spring.Blog.model.User;
import com.spring.Blog.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.spring.Blog.utility.user.UserRoles.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class UserControllerTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final Long FAKE_ID = 1L;
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGettingAllUsersThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/users")
                        .content(mapper.writeValueAsString(userListBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGettingAllAdminsThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/admins")
                        .content(mapper.writeValueAsString(userListBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGettingUserByIdThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/users/" + FAKE_ID)
                        .content(mapper.writeValueAsString(userBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGettingAdminByIdThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/admins/" + FAKE_ID)
                        .content(mapper.writeValueAsString(userBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private List<User> userListBuilder() {
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            User user = userBuilder();
            if (i > 1) {
                user.setUsername("username" + "_" + i);
                user.setEmail("user" + i + "@mail.com");
                user.setPassword("p@ssw0rd");
                user.setRole(USER.getRole());
            }
            users.add(user);
        }
        return users;
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
