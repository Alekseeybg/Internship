package com.spring.Blog.service;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.repository.BlogRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.EntityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityUtility entityUtility;

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }



    public Article addArticle(Article article, String username, Long blogId) {
        if (blogId == null || username.isEmpty()) {
            throw new UnprocessableEntityException("Invalid request");
        }
        User user = entityUtility.getUserByUsername(username);
        Blog blog = entityUtility.getBlogById(blogId);

        if (entityUtility.userLogged(user) && (entityUtility.userIsOwner(blog, user) || entityUtility.userIsAdmin(user))) {
            //TODO: FIX when you add an article as admin to another user's blog it sets the owner as admin
            return saveArticle(article, blog, user);
        }
        throw new UnauthorizedException("UNAUTHORIZED request");
    }

    public Article saveArticle(Article article, Blog blog, User user) {
        article.setBlog(blog);
        article.setAuthor(user);
        blog.setOwner(user);
        blog.addArticle(article);

        return articleRepository.save(article);
    }
}