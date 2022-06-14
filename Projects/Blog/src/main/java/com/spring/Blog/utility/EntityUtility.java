package com.spring.Blog.utility;

import com.spring.Blog.model.Article;
import com.spring.Blog.model.Blog;
import com.spring.Blog.model.Image;
import com.spring.Blog.model.User;

import com.spring.Blog.repository.ArticleRepository;
import com.spring.Blog.repository.BlogRepository;
import com.spring.Blog.repository.ImageRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.ExceptionMessages;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnprocessableEntityException;
import com.spring.Blog.utility.user.ValidationMessages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.spring.Blog.utility.user.UserValidator.*;
import static com.spring.Blog.utility.user.UserRoles.*;

@Component
@AllArgsConstructor
public class EntityUtility {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ImageRepository imageRepository;



    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean correctPassword(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new UnprocessableEntityException("Incorrect password");
        }
        return true;
    }

    public boolean userLogged(User user) {
        return user.isLogged();
    }

    public boolean userIsBlogOwner(Blog blog, User user) {
        return blog.getOwner().equals(user);
    }

    public boolean userIsArticleAuthor(Article article, User user) {
        return article.getAuthor().equals(user);
    }
    public boolean userIsAdmin(User user) {
        return user.getRole().equals(ADMIN.getRole());
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            String message = ExceptionMessages.USER_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            String message = ExceptionMessages.USER_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        }
        return user;
    }

    public User getUserById(long userId) {
        String message = ExceptionMessages.USER_NOT_FOUND.getMessage();
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    public Blog getBlogById(long blogId) {
        String message = ExceptionMessages.BLOG_NOT_FOUND.getMessage();
        return blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    public List<Blog> getBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        if (blogs.isEmpty()) {
            String message = ExceptionMessages.BLOGS_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        }
        return blogs;
    }

    public List<Article> getArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            String message = ExceptionMessages.ARTICLES_NOT_FOUND.getMessage();
            throw new ResourceNotFoundException(message);
        }
        return articles;
    }

    public Article getArticleById(long articleId) {
        String message = ExceptionMessages.ARTICLE_NOT_FOUND.getMessage();
        return articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    public boolean articleImageExists(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.ARTICLE_NOT_FOUND.getMessage()));
        return article.getImage() != null;
    }

    public void replaceImage(long articleId, Image image) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.ARTICLE_NOT_FOUND.getMessage()));
        article.setImage(image);
        articleRepository.save(article);
    }

    public Image getImageById(long imageId) {
        String message = ExceptionMessages.IMAGE_NOT_FOUND.getMessage();
        return imageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    public void deleteImageIfExists(Image image) {
        if (image == null) {
            throw new ResourceNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND.getMessage());
        }
        imageRepository.delete(image);
        imageRepository.flush();
    }
}