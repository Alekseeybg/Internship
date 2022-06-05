package com.spring.Blog.service;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.repository.BlogRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.user.UserUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtility userUtility;

    public List<Article> getArticles() {
        return articleRepository.findAll().isEmpty() ? null : articleRepository.findAll();
    }

    public boolean blogExists(long blogId) {
        return blogRepository.findById(blogId).isPresent();
    }

    public ResponseEntity<String> addArticle(Article article, String username, long blogId) {
        User user;
        Blog blog;

        if (userUtility.userExists(username)) {
            user = userRepository.findByUsername(username);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (blogExists(blogId)) {
            blog = blogRepository.findById(blogId).get();
            if (userUtility.userLogged(user)) {
                article.setBlog(blog);
                blog.setOwner(user);
                blog.addArticle(article);
                articleRepository.save(article);
                return new ResponseEntity<>("Article created successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("UNAUTHORIZED request", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Blog does not exist", HttpStatus.NOT_FOUND);
        }
    }
}