package com.spring.Blog.repository;

import com.spring.Blog.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByFilename(String filename);
    Image findByArticleId(Long articleId);
    Optional<Image> findById(Long id);
}

