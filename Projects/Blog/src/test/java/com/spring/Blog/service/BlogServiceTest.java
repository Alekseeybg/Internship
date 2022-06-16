package com.spring.Blog.service;

import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.BlogRepository;
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

import static com.spring.Blog.utility.exception.ExceptionMessages.BLOGS_NOT_FOUND;
import static com.spring.Blog.utility.user.UserRoles.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BlogServiceTest {

    @InjectMocks
    private BlogService blogService;
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private EntityUtility entityUtility;
    private User user;
    private Blog blog;
    private List<Blog> blogs;
    private List<Blog> emptyList;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createFakeBlogs();
    }

    @Test
    public void givenNoBlogsInRepositoryWhenLookingForBlogsThrowException() {
        List<Blog> blogs1 = new ArrayList<>();
        when(entityUtility.getBlogs()).thenReturn(emptyList);
        try {
            blogs1 = blogService.getBlogs();
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), BLOGS_NOT_FOUND.getMessage());
        }
        assertEquals(blogs1, emptyList);
    }

    @Test
    public void givenBlogsInRepositoryWhenLookingForBlogsThenReturnBlogs() {
        List<Blog> blogs1;
        when(entityUtility.getBlogs()).thenReturn(blogs);
        blogs1 = blogService.getBlogs();
        assertEquals(blogs1, blogs);
    }

    @Test
    public void givenBlogAndWrongUsernameWhenAddBlogThenThrowException() {
        when(entityUtility.getUserByUsername("wrongUsername")).thenThrow(new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
        try {
            blogService.addBlog(any(Blog.class), "wrongUsername");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.USER_NOT_FOUND.getMessage());
        }
        verify(blogRepository, never()).save(any(Blog.class));
    }

    @Test
    public void givenBlogAndCorrectNotAuthenticatedUsernameWhenAddBlogThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(false);
        try {
            blogService.addBlog(blog, "username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(blogRepository, never()).save(blog);
    }

    @Test
    public void givenBlogAndCorrectAuthenticatedUsernameWhenAddBlogThenReturnUser() {
        blog = new Blog("Blog Title");
        Blog blog1 = new Blog();
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(true);
        try {
            when(blogRepository.save(blog)).thenReturn(blog);
            blog1 = blogService.addBlog(blog, anyString());
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(blogRepository, times(1)).save(blog);
        assertEquals(blog.getTitle(), blog1.getTitle());
    }

    @Test
    public void givenWrongUsernameWhenDeleteBlogThenThrowException() {
        when(entityUtility.getUserByUsername("wrongUsername")).thenThrow(new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
        try {
            blogService.deleteBlog(anyLong(), "wrongUsername");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.USER_NOT_FOUND.getMessage());
        }
        verify(blogRepository, never()).deleteById(anyLong());
    }

    @Test
    public void givenWrongIdWhenDeleteBlogThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(true);
        when(entityUtility.getBlogById(anyLong())).thenThrow(new ResourceNotFoundException(ExceptionMessages.BLOG_NOT_FOUND.getMessage()));
        try {
            blogService.deleteBlog(anyLong(), "username");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.BLOG_NOT_FOUND.getMessage());
        }
        verify(blogRepository, never()).deleteById(anyLong());
    }

    @Test
    public void givenCorrectIdAndUsernameNotAuthenticatedWhenDeleteBlogThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getBlogById(anyLong())).thenReturn(blog);
        when(entityUtility.userLogged(user)).thenReturn(false);
        try {
            blogService.deleteBlog(anyLong(), "username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(blogRepository, never()).deleteById(anyLong());
    }

    @Test
    public void givenCorrectIdAndUsernameAuthenticatedWhenDeleteBlogThenVerifyBlogDeleted() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getBlogById(anyLong())).thenReturn(blog);
        when(entityUtility.userLogged(user)).thenReturn(true);
        try {
            blogService.deleteBlog(anyLong(), "username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(blogRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void givenWrongUsernameWhenUpdateBlogThenThrowException() {
        when(entityUtility.getUserByUsername("wrongUsername")).thenThrow(new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
        try {
            blogService.updateBlog(blog, 1L, "wrongUsername");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.USER_NOT_FOUND.getMessage());
        }
        verify(blogRepository, never()).save(blog);
    }

    @Test
    public void givenWrongIdWhenUpdateBlogThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.userLogged(user)).thenReturn(true);
        when(entityUtility.getBlogById(anyLong())).thenThrow(new ResourceNotFoundException(ExceptionMessages.BLOG_NOT_FOUND.getMessage()));
        try {
            blogService.updateBlog(blog, 1L, "Username");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), ExceptionMessages.BLOG_NOT_FOUND.getMessage());
        }
        verify(blogRepository, never()).save(blog);
    }

    @Test
    public void givenCorrectIdAndUsernameNotAuthenticatedWhenUpdateBlogThenThrowException() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getBlogById(anyLong())).thenReturn(blog);
        when(entityUtility.userLogged(user)).thenReturn(false);
        try {
            blogService.updateBlog(blog, 1L, "Username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(blogRepository, never()).save(blog);
    }

    @Test
    public void givenCorrectIdAndUsernameAuthenticatedWhenUpdateBlogThenReturnUpdatedBlog() {
        when(entityUtility.getUserByUsername(anyString())).thenReturn(user);
        when(entityUtility.getBlogById(anyLong())).thenReturn(blog);
        when(entityUtility.userLogged(user)).thenReturn(true);
        try {
            blogService.updateBlog(blog, 1L, "Username");
        } catch (UnauthorizedException e) {
            assertEquals(e.getMessage(), ExceptionMessages.UNAUTHORIZED.getMessage());
        }
        verify(blogRepository, times(1)).save(blog);
    }

    private void createFakeBlogs() {
        user = new User("User", "user@mail.com", "p@SSw0rd", USER.getRole());
        blog = new Blog("Blog Title");

        blogs = new ArrayList<>();
        blogs.add((blog));

        emptyList = Collections.emptyList();
    }
}
