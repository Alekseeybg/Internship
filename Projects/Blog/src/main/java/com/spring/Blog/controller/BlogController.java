package com.spring.Blog.controller;

import com.spring.Blog.model.Blog;
import com.spring.Blog.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/blogs/")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<List<Blog>> getBlogs() {
        return new ResponseEntity<>(blogService.getBlogs(), HttpStatus.OK);
    }

    @PostMapping(path = "new")
    public ResponseEntity<Blog> addBlogToUser(@RequestBody Blog blog, @RequestParam(name = "user") String username) {
        return blogService.addBlog(blog, username);
    }

    @DeleteMapping(path = "{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable("blogId") long blogId, @RequestParam(name = "user") String username) {
        return blogService.deleteBlog(blogId, username);
    }

    @PutMapping(path = "{blogId}")
    public ResponseEntity<String> updateBlog(@RequestBody Blog blog, @PathVariable("blogId") long blogId, @RequestParam(name = "user") String username) {
        return blogService.updateBlog(blog, blogId, username);
    }
}
