package com.spring.Blog;

import com.spring.Blog.service.ImageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class BlogApplication implements CommandLineRunner {
	@Resource
	ImageService imageService;
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		imageService.deleteAll();
		imageService.init();
	}
}