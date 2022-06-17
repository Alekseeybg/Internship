package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.Blog.BlogApplication;
import com.spring.Blog.service.ArticleService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class ArticleControllerTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1/articles";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;
}
