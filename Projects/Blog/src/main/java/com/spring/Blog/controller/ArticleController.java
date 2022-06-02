package com.spring.Blog.controller;

import com.spring.Blog.model.Article;
import com.spring.Blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/articles/")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

    @PostMapping
    @RequestMapping(path = "new-article")
    public ResponseEntity<String> addArticle(@RequestBody Article article,
                                             @RequestParam(name = "user", defaultValue = "username") String username,
                                             @RequestParam(name = "blogId", defaultValue = "1") long blogId) {
        return articleService.addArticle(article, username, blogId);
    }
}
