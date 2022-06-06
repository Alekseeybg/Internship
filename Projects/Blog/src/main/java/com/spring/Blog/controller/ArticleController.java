package com.spring.Blog.controller;

import com.spring.Blog.model.Article;
import com.spring.Blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<Article>> getArticles() {
        List<Article> articles = articleService.getArticles();
        return articles.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(path = "/new")
    public ResponseEntity<String> addArticle(@RequestBody Article article,
                                             @RequestParam(name = "user", defaultValue = "username") String username,
                                             @RequestParam(name = "blogId", defaultValue = "1") long blogId) {
        return articleService.addArticle(article, username, blogId);
    }
}
