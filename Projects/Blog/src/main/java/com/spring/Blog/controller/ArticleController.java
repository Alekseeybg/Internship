package com.spring.Blog.controller;

import com.spring.Blog.model.Article;
import com.spring.Blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    @RequestMapping(path = "articles")
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

    @PostMapping
    @RequestMapping(path = "{username}/blogs/{blogId}/articles/new")
    public ResponseEntity<String> addArticle(@RequestBody Article article, @PathVariable("username") String username, @PathVariable("blogId") long blogId) {
        return articleService.addArticle(article, username, blogId);
    }
}
