package com.spring.Blog.controller;

import com.spring.Blog.model.Article;
import com.spring.Blog.service.ArticleService;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
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
    public ResponseEntity<Article> addArticle(@RequestBody Article article,
                                              @RequestParam(name = "user") String username,
                                              @RequestParam(name = "blogId") Long blogId) {

        Article newArticle = articleService.addArticle(article, username, blogId);
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@RequestBody Article article,
                                                 @PathVariable(name = "id") Long id,
                                                 @RequestParam(name = "user") String username) {
        Article updatedArticle = articleService.updateArticle(article, id, username);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable("id") Long articleId,
                                                @RequestParam(name = "user") String username) {
        articleService.deleteArticle(articleId, username);
        return new ResponseEntity<>("Article deleted", HttpStatus.OK);
    }
}
