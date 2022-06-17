package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.Blog.BlogApplication;
import com.spring.Blog.model.Article;
import com.spring.Blog.service.ArticleService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class ArticleControllerTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1/articles";
    private static final String USERNAME = "username";
    private static final Long FAKE_ID = 1L;
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<Article> articleCaptor;

    @Test
    public void whenGettingAllArticlesThenReturn200() throws Exception {
        when(articleService.getArticles()).thenReturn(articleListBuilder());
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .content(mapper.writeValueAsString(articleListBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(articleService, times(1)).getArticles();
    }

    @Test
    public void whenAddArticleThenReturn201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/?user=" + USERNAME + "&blogId=" + FAKE_ID)
                        .content(mapper.writeValueAsString(articleBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(articleService, times(1)).addArticle(articleCaptor.capture(), eq(USERNAME), eq(FAKE_ID));
    }

    @Test
    public void whenUpdateArticleThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + FAKE_ID + "/?user=" + USERNAME)
                        .content(mapper.writeValueAsString(articleBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(articleService, times(1)).updateArticle(articleCaptor.capture(), eq(FAKE_ID), eq(USERNAME));
    }

    @Test
    public void whenDeleteArticleThenReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + FAKE_ID + "/?user=" + USERNAME)
                        .content(mapper.writeValueAsString(articleBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(articleService, times(1)).deleteArticle(eq(FAKE_ID), eq(USERNAME));
    }

    private List<Article> articleListBuilder() {
        List<Article> articles = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Article article = articleBuilder();
            if (i > 1) {
                article.setTitle("Test title" + i);
                article.setContent("Test content" + i);
            }
            articles.add(article);
        }
        return articles;
    }

    private Article articleBuilder() {
        Article article = new Article();
        article.setTitle("Test title");
        article.setContent("Test content");
        return article;
    }
}