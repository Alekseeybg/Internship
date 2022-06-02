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

    public ResponseEntity<List<Blog>> getBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(blogs, HttpStatus.OK);
    }
    public boolean blogExists(long blogId) {
        return blogRepository.findById(blogId).isPresent();
    }
    public ResponseEntity<Blog> addBlog(Blog blog, String username) {
        if (!UserUtility.userExists(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByUsername(username);
        if (UserUtility.userLogged(user)) {
            blog.setUser(user);
            return new ResponseEntity<>(blogRepository.save(blog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}