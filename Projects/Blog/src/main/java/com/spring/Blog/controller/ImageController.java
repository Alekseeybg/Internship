package com.spring.Blog.controller;

import com.spring.Blog.model.Image;
import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.repository.ImageRepository;
import com.spring.Blog.service.ImageService;
import com.spring.Blog.utility.EntityUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/files")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private EntityUtility entityUtility;

    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/{articleId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                            @PathVariable("articleId") long article_id) {
        imageService.uploadFile(file, article_id);
        return new ResponseEntity<>("Image uploaded successfully!", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Image>> getListFiles() {
        return new ResponseEntity<>(imageService.getAllFiles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getFile(@PathVariable("id") long id) {
        return new ResponseEntity<>(imageService.getFile(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable("id") long id, @RequestParam(name = "user") String username) {
        imageService.delete(id);
        return new ResponseEntity<>("Image deleted!", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/view/{filename:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> showFile(@PathVariable String filename) throws IOException {
        ClassPathResource imageFile = new ClassPathResource("static/images/" + filename);
        byte[] imageBytes = StreamUtils.copyToByteArray(imageFile.getInputStream());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
