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
@RequestMapping(path = "api/v1/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<List<Blog>> getBlogs() {
        return new ResponseEntity<>(blogService.getBlogs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable("id") Long id) {
        return new ResponseEntity<>(blogService.getBlog(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addBlogToUser(@RequestBody Blog blog, @RequestParam(name = "user") String username) {
        blogService.addBlog(blog, username);
        return new ResponseEntity<>("Blog created successfully!", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable("blogId") long blogId, @RequestParam(name = "user") String username) {
        blogService.deleteBlog(blogId, username);
        return new ResponseEntity<>("Blog deleted", HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{blogId}")
    public ResponseEntity<String> updateBlog(@RequestBody Blog blog, @PathVariable("blogId") long blogId, @RequestParam(name = "user") String username) {
        blogService.updateBlog(blog, blogId, username);
        return new ResponseEntity<>("Blog updated successfully", HttpStatus.OK);
    }
}
