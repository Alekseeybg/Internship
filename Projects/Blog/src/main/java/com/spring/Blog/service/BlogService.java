package com.spring.Blog.service;

import com.spring.Blog.model.Blog;
import com.spring.Blog.model.User;
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
@AllArgsConstructor
public class BlogService {
    @Autowired
    private final BlogRepository blogRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private UserUtility userUtility;


    public ResponseEntity<List<Blog>> getBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    public ResponseEntity<Blog> addBlog(Blog blog, String username) {
        if (!userUtility.userExists(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByUsername(username);

        if (userUtility.userLogged(user)) {
            blog.setOwner(user);
            return new ResponseEntity<>(blogRepository.save(blog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> deleteBlog(long blogId, String username) {
        if (!userUtility.userExists(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByUsername(username);

        if (userUtility.userLogged(user)) {
            if (blogRepository.findById(blogId).isPresent()) {
                Blog blogToDelete = blogRepository.findById(blogId).get();
                if (userUtility.userIsOwner(blogToDelete, user)) {
                    blogRepository.deleteById(blogId);
                    return new ResponseEntity<>("Blog deleted successfully",HttpStatus.OK);
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

    public ResponseEntity<String> updateBlog(Blog blog, long blogId, String username) {
        if (!userUtility.userExists(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByUsername(username);

        if (userUtility.userLogged(user)) {
            if (blogRepository.findById(blogId).isPresent()) {
                Blog blogToUpdate = blogRepository.findById(blogId).get();
                if (userUtility.userIsOwner(blogToUpdate, user)) {
                    blogToUpdate.setTitle(blog.getTitle());
                    blogRepository.save(blogToUpdate);
                    return new ResponseEntity<>("Blog updated successfully",HttpStatus.OK);
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

}