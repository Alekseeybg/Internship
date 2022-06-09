package com.spring.Blog.repository;

import com.spring.Blog.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByFilename(String filename);
    Image findByArticleId(Long articleId);
}

