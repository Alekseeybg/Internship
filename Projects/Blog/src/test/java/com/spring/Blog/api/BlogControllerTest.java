package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.Blog.BlogApplication;
import com.spring.Blog.model.Blog;
import com.spring.Blog.service.BlogService;
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

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class BlogControllerTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1/blogs";
    private static final Long FAKE_ID = 1L;
    private static final String USERNAME = "username";
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BlogService blogService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGettingAllBlogsThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .content(mapper.writeValueAsString(blogListBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGettingBlogThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + FAKE_ID)
                        .content(mapper.writeValueAsString(blogBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAddingBlogThenReturn201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "?user=" + USERNAME)
                        .content(mapper.writeValueAsString(blogBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenDeletingBlogThenReturn204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + FAKE_ID + "?user=" + USERNAME)
                        .content(mapper.writeValueAsString(blogBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdatingBlogThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + FAKE_ID + "?user=" + USERNAME)
                        .content(mapper.writeValueAsString(blogBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private List<Blog> blogListBuilder() {
        List<Blog> blogs = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Blog blog = blogBuilder();
            if (i > 1) {
                blog.setTitle("Test title" + i);
            }
            blogs.add(blog);
        }
        return blogs;
    }

    private Blog blogBuilder() {
        Blog blog = new Blog();
        blog.setTitle("Test title");
        return blog;
    }
}
