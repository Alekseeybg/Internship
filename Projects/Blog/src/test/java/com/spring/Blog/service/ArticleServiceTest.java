package com.spring.Blog.service;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import com.spring.Blog.model.Image;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ExceptionMessages;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.spring.Blog.utility.exception.ExceptionMessages.*;
import static com.spring.Blog.utility.user.UserRoles.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ArticleServiceTest {
    @InjectMocks
    private ArticleService articleService;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private EntityUtility entityUtility;
    private User user;

    private Blog blog;
    private Image image;
    private Article article;
    private List<Article> articles;
    private List<Article> emptyList;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createFakeArticles();
    }

    @Test
    public void givenNoArticlesInRepositoryWhenLookingForArticlesThrowException() {
        List<Article> articles1 = new ArrayList<>();
        when(entityUtility.getArticles()).thenThrow(new ResourceNotFoundException(ARTICLES_NOT_FOUND.getMessage()));
        try {
            articles1 = articleService.getArticles();
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ARTICLES_NOT_FOUND.getMessage());
        }
        assertEquals(articles1, emptyList);
    }

    @Test
    public void givenArticlesInRepositoryWhenLookingForArticlesThenReturnArticles() {
        List<Article> articles1;
        when(entityUtility.getArticles()).thenReturn(articles);
        articles1 = articleService.getArticles();
        assertEquals(articles1, articles);
    }

    //Add Article
    @Test
    public void givenArticleAndWrongUsernameWhenAddArticleThenThrowException() {
        when(entityUtility.getUserByUsername("wrongUsername")).thenThrow(new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
        try {
            articleService.addArticle(article, "wrongUsername", 1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.USER_NOT_FOUND.getMessage());
        }
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    public void givenWrongBlogIdAndCorrectUsernameWhenAddArticleThenThrowException() {
        when(entityUtility.getUserByUsername("User")).thenReturn(user);
        when(entityUtility.getBlogById(1L)).thenThrow(new ResourceNotFoundException(ExceptionMessages.BLOG_NOT_FOUND.getMessage()));
        try {
            articleService.addArticle(article, "User", 1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.BLOG_NOT_FOUND.getMessage());
        }
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    public void givenArticleAndCorrectNotAuthenticatedUsernameWhenAddArticleThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(false);
        try {
            articleService.addArticle(article, "username", 1L);
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(articleRepository, never()).save(article);
    }

    @Test
    public void givenArticleAndCorrectAuthenticatedUsernameWhenAddArticleThenReturnArticle() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(true);
        when(entityUtility.getBlogById(anyLong())).thenReturn(blog);
        when(entityUtility.userIsBlogOwner(blog, user)).thenReturn(true);
        when(articleService.addArticle(article, "username", 1L)).thenReturn(article);

        Article article1 = articleService.addArticle(article, "username", 1L);
        assertEquals(article1, article);
        verify(articleRepository, times(1)).save(article);
    }

    //Delete Article

    @Test
    public void givenWrongUsernameWhenDeleteArticleThenThrowException() {
        when(entityUtility.getUserByUsername("wrongUsername")).thenThrow(new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
        try {
            articleService.deleteArticle(anyLong(), "wrongUsername");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.USER_NOT_FOUND.getMessage());
        }
        verify(articleRepository, never()).deleteById(anyLong());
    }

    @Test
    public void givenWrongIdWhenDeleteArticleThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(true);
        when(entityUtility.getArticleById(anyLong())).thenThrow(new ResourceNotFoundException(ExceptionMessages.BLOG_NOT_FOUND.getMessage()));
        try {
            articleService.deleteArticle(anyLong(), "username");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.BLOG_NOT_FOUND.getMessage());
        }
        verify(articleRepository, never()).deleteById(anyLong());
    }

    @Test
    public void givenCorrectIdAndUsernameNotAuthenticatedWhenDeleteArticleThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getArticleById(anyLong())).thenReturn(article);
        when(entityUtility.userLogged(user)).thenReturn(false);
        try {
            articleService.deleteArticle(anyLong(), "username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(articleRepository, never()).deleteById(anyLong());
    }

    @Test
    public void givenCorrectIdAndUsernameAuthenticatedWhenDeleteArticleThenVerifyArticleDeleted() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getArticleById(anyLong())).thenReturn(article);
        when(entityUtility.userLogged(user)).thenReturn(true);
        try {
            articleService.deleteArticle(anyLong(), "username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(articleRepository, times(1)).delete(article);
    }

    //Update Article

    @Test
    public void givenWrongUsernameWhenUpdateArticleThenThrowException() {
        when(entityUtility.getUserByUsername("wrongUsername")).thenThrow(new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
        try {
            articleService.updateArticle(article, 1L, "wrongUsername");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.USER_NOT_FOUND.getMessage());
        }
        verify(articleRepository, never()).save(article);
    }

    @Test
    public void givenWrongIdWhenUpdateArticleThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(true);
        when(entityUtility.getArticleById(anyLong())).thenThrow(new ResourceNotFoundException(ARTICLE_NOT_FOUND.getMessage()));
        try {
            articleService.updateArticle(article, 1L, "Username");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.ARTICLE_NOT_FOUND.getMessage());
        }
        verify(articleRepository, never()).save(article);
    }

    @Test
    public void givenCorrectIdAndUsernameNotAuthenticatedWhenUpdateArticleThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getArticleById(anyLong())).thenReturn(article);
        when(entityUtility.userLogged(user)).thenReturn(false);
        try {
            articleService.updateArticle(article, 1L, "Username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(articleRepository, never()).save(article);
    }

    @Test
    public void givenCorrectIdAndUsernameAuthenticatedWhenUpdateArticleThenReturnUpdatedArticle() {
        Article article1 = new Article("title", "content");

        when(entityUtility.getUserByUsername("User")).thenReturn(user);
        when(entityUtility.getArticleById(anyLong())).thenReturn(article);
        when(entityUtility.userLogged(user)).thenReturn(true);
        when(entityUtility.userIsArticleAuthor(article, user)).thenReturn(true);

        articleService.updateArticle(article1, 1L, "User");

        verify(articleRepository, times(1)).save(article);
        assertEquals(article1.getTitle(), article.getTitle());
        assertEquals(article1.getContent(), article.getContent());
    }


    private void createFakeArticles() {
        user = new User("User", "user@mail.com", "p@SSw0rd", USER.getRole());
        image = new Image("image", "image.jpg");
        blog = new Blog("Blog Title");
        article = new Article(1L, "Article Title", "Article Content", blog, user, image);

        articles = new ArrayList<>();
        articles.add((article));

        emptyList = Collections.emptyList();
    }
}