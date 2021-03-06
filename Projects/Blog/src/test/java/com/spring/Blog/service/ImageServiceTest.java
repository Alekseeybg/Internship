package com.spring.Blog.service;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import com.spring.Blog.model.Image;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.ImageRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.spring.Blog.utility.exception.ExceptionMessages.*;
import static com.spring.Blog.utility.user.UserRoles.USER;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageServiceTest {
    @InjectMocks
    private ImageService imageService;
    @Mock
    private EntityUtility entityUtility;
    @Mock
    private ImageRepository imageRepository;
    @Value("${images.root.path}")
    private String root;
    private User user;
    private Blog blog;
    private Image image;
    private Article article;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createFakeImages();
    }

    @Test
    public void givenWrongArticleIdWhenUploadFileThenThrowException() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image".getBytes());
        when(entityUtility.articleImageExists(anyLong())).thenReturn(false);
        try {
            imageService.uploadFile(file, 1L);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Could not store file " + file.getOriginalFilename() + ". Please try again!");
        }
        verify(imageRepository, never()).save(image);
    }

    @Test
    public void givenWrongArticleIdWhenDeleteImageThenThrowException() {
        when(entityUtility.getImageById(anyLong())).thenThrow(new ResourceNotFoundException(IMAGE_NOT_FOUND.getMessage()));
        try {
            imageService.delete(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), IMAGE_NOT_FOUND.getMessage());
        }
        verify(entityUtility, never()).deleteImageIfExists(image);
    }

    @Test
    public void givenCorrectArticleIdWhenDeleteImageThenDeleteImage() {
        when(entityUtility.getImageById(anyLong())).thenReturn(image);
        try {
            imageService.delete(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), IMAGE_NOT_FOUND.getMessage());
        }
        verify(entityUtility, times(1)).deleteImageIfExists(image);
    }

    @Test
    public void givenIncorrectImageIdWhenGetImageThenThrowException() {
        when(entityUtility.getImageById(anyLong())).thenThrow(new ResourceNotFoundException(IMAGE_NOT_FOUND.getMessage()));
        try {
           imageService.getFile(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), IMAGE_NOT_FOUND.getMessage());
        }
    }

    private void createFakeImages() {
        user = new User("User", "user@mail.com", "p@SSw0rd", USER.getRole());
        blog = new Blog("Blog Title");
        article = new Article("Article Title", "Article Content");
        image = new Image("image", "image.jpg");
    }
}