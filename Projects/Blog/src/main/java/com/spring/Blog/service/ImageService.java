package com.spring.Blog.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Image;
import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.repository.ImageRepository;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ExceptionMessages;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    //private final Path root = Paths.get("src/main/resources/static/images");
    @Value("${images.root.path}")
    private String root;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private EntityUtility entityUtility;
    @Autowired
    private ArticleRepository articleRepository;

    public void init() {
        try {
            Path path = Paths.get(root);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Image uploadFile(MultipartFile file, long article_id) {
        Image image;

        try {
            saveFile(file);
            String url = "http://localhost:8080/api/v1/files/" + file.getOriginalFilename();
            image = new Image(file.getOriginalFilename(), url);
            if (entityUtility.articleImageExists(article_id)) {
                entityUtility.replaceImage(article_id, image);
            } else {
                Article article = entityUtility.getArticleById(article_id);
                image.setArticle(article);
                return imageRepository.save(image);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        return image;
    }

    private void saveFile(MultipartFile file) {
        try {
            File file1 = new File(Paths.get(root) + "/" + file.getOriginalFilename());
            if (file1.exists()) {
                deleteFile(file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), Paths.get(root).resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!");
        }
    }

    private void deleteFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get(root).resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file " + filename + ". Please try again!");
        }
    }

    public List<Image> getAllFiles() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND.getMessage());
        }
        return images;
    }

    public Image getFile(long id) {
        return entityUtility.getImageById(id);
    }

    public String delete(long id) {
        try {
            Image image = entityUtility.getImageById(id);
            if (entityUtility.deleteImageIfExists(image)) {
                Files.deleteIfExists(Paths.get(root).resolve(image.getFilename()));
                imageRepository.delete(image);
            }
        } catch (IOException e) {
            throw new UnprocessableEntityException("Could not delete file. Error: " + e.getMessage());
        }
        return "Image deleted successfully";
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get("resources/").toFile());
        FileSystemUtils.deleteRecursively(Paths.get(root).toFile());
        imageRepository.deleteAll();
    }
}