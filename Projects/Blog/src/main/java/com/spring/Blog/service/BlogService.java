package com.spring.Blog.service;

import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.BlogRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.EntityUtility;
import com.spring.Blog.utility.exception.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityUtility entityUtility;


    public List<Blog> getBlogs() {
        return entityUtility.getBlogs();
    }

    public Blog addBlog(Blog blog, String username) {
        User user = entityUtility.getUserByUsername(username);

        if (!entityUtility.userLogged(user)) {
            String message = ExceptionMessages.UNAUTHORIZED.getMessage();
            throw new UnauthorizedException(message);
        }
        blog.setOwner(user);
        return blogRepository.save(blog);
    }

    public void deleteBlog(long blogId, String username) {
        User user = entityUtility.getUserByUsername(username);
        Blog blogToDelete = entityUtility.getBlogById(blogId);

        if (!entityUtility.userLogged(user) && (!entityUtility.userIsBlogOwner(blogToDelete, user) || !entityUtility.userIsAdmin(user))) {
            String message = ExceptionMessages.UNAUTHORIZED.getMessage();
            throw new UnauthorizedException(message);
        }
        blogRepository.deleteById(blogId);
    }

    public Blog updateBlog(Blog blog, long blogId, String username) {
        User user = entityUtility.getUserByUsername(username);
        Blog blogToUpdate = entityUtility.getBlogById(blogId);

        if (!entityUtility.userLogged(user) && (!entityUtility.userIsBlogOwner(blogToUpdate, user) || !entityUtility.userIsAdmin(user))) {
            String message = ExceptionMessages.UNAUTHORIZED.getMessage();
            throw new UnauthorizedException(message);
        }
        blogToUpdate.setTitle(blog.getTitle());
        return blogRepository.save(blogToUpdate);
    }
}