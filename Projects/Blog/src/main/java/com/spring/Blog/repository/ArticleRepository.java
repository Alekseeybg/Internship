package com.spring.Blog.repository;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByBlog(Blog blog);

    Article findByTitle(String title);
}
