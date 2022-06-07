package com.spring.Blog.service;

import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
import com.spring.Blog.repository.BlogRepository;
import com.spring.Blog.repository.UserRepository;
import com.spring.Blog.utility.exception.ResourceNotFoundException;
import com.spring.Blog.utility.exception.UnauthorizedException;
import com.spring.Blog.utility.EntityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<Blog> blogs = blogRepository.findAll();
        if (blogs.isEmpty()) {
            throw new ResourceNotFoundException("No blogs found");
        } else {
            return blogs;
        }
    }

    public Blog addBlog(Blog blog, String username) {
        if (!entityUtility.userExists(username)) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findByUsername(username);

        if (entityUtility.userLogged(user)) {
            blog.setOwner(user);
            return blogRepository.save(blog);
        } else {
            throw new UnauthorizedException("UNAUTHORIZED request");
        }
    }

    public ResponseEntity<String> deleteBlog(long blogId, String username) {
        if (!entityUtility.userExists(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByUsername(username);

        if (entityUtility.userLogged(user)) {
            if (blogRepository.findById(blogId).isPresent()) {
                Blog blogToDelete = blogRepository.findById(blogId).get();
                if (entityUtility.userIsOwner(blogToDelete, user)) {
                    blogRepository.deleteById(blogId);
                    return new ResponseEntity<>("Blog deleted successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public Blog updateBlog(Blog blog, long blogId, String username) {
        User user = entityUtility.getUserByUsername(username);
        Blog blogToUpdate = entityUtility.getBlogById(blogId);
        if (entityUtility.userLogged(user) && entityUtility.userIsOwner(blogToUpdate, user)) {
            blogToUpdate.setTitle(blog.getTitle());
            return blogRepository.save(blogToUpdate);
        } else {
           throw new UnauthorizedException("UNAUTHORIZED request");
        }
    }
}