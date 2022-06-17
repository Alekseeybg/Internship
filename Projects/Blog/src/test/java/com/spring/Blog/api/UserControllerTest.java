package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.Blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public class UserControllerTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
}
