package com.spring.Blog.service;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.exception.ExceptionMessages;
import com.spring.Blog.utility.EntityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EntityUtility entityUtility;

    public List<Article> getArticles() {
        return entityUtility.getArticles();
    }

    public Article addArticle(Article article, String username, Long blogId) {
        User user = entityUtility.getUserByUsername(username);
        Blog blog = entityUtility.getBlogById(blogId);

        if (entityUtility.userLogged(user) && (entityUtility.userIsBlogOwner(blog, user) || entityUtility.userIsAdmin(user))) {
            return saveArticle(article, blog, user);
        }
        String message = ExceptionMessages.UNAUTHORIZED.getMessage();
        throw new UnauthorizedException(message);
    }

    public Article updateArticle(Article article, Long articleId, String username) {

        User user = entityUtility.getUserByUsername(username);
        Article articleToUpdate = entityUtility.getArticleById(articleId);

        if (entityUtility.userLogged(user) && (entityUtility.userIsArticleAuthor(articleToUpdate, user) || entityUtility.userIsAdmin(user))) {
            articleToUpdate.setTitle(article.getTitle());
            articleToUpdate.setContent(article.getContent());
            return articleRepository.save(articleToUpdate);
        }
        String message = ExceptionMessages.UNAUTHORIZED.getMessage();
        throw new UnauthorizedException(message);
    }

    public void deleteArticle(long articleId, String username) {
        User user = entityUtility.getUserByUsername(username);
        Article articleToDelete = entityUtility.getArticleById(articleId);
        if (!entityUtility.userLogged(user) && !(entityUtility.userIsArticleAuthor(articleToDelete, user) || entityUtility.userIsAdmin(user))) {
            String message = ExceptionMessages.UNAUTHORIZED.getMessage();
            throw new UnauthorizedException(message);
        }
         articleRepository.delete(articleToDelete);
    }

    private Article saveArticle(Article article, Blog blog, User user) {
        article.setBlog(blog);
        article.setAuthor(user);
        //blog.setOwner(user);
        blog.addArticle(article);
        return articleRepository.save(article);
    }
}