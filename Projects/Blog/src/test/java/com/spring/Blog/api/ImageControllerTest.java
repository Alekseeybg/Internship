package com.spring.Blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spring.Blog.BlogApplication;
import com.spring.Blog.model.Article;
import com.spring.Blog.model.Image;
import com.spring.Blog.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;


import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class ImageControllerTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final Long FAKE_ID = 1L;
    private final ObjectMapper mapper = new ObjectMapper();
    @Value("${images.root.path}")
    private String root;

    @MockBean
    private ImageService imageService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void whenUploadingImageThenReturn201() throws Exception {

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "file.jpg",
                IMAGE_JPEG_VALUE,
                "some image bytes".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(BASE_URL + "/files/" + FAKE_ID)
                        .file(file))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenDeleteImageThenReturn204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/files/" + FAKE_ID)
                        .content(mapper.writeValueAsString(imageBuilder()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Image imageBuilder() {
        Image image = new Image();
        image.setId(FAKE_ID);
        image.setFilename("filename");
        image.setUrl("url");
        image.setArticle(articleBuilder());
        return image;
    }

    private Article articleBuilder() {
        Article article = new Article();
        article.setId(FAKE_ID);
        article.setTitle("Test title");
        article.setContent("Test content");
        return article;
    }

    private MultipartFile imageBuilder2() {
        return new MockMultipartFile("file", "filename.jpg", "image/jpeg", "some_bytes".getBytes());
    }
}
