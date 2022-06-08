package com.spring.Blog.utility.exception;

public enum ExceptionMessages {
    USER_NOT_FOUND("User not found!"),
    USERS_NOT_FOUND("No users found!"),
    ADMIN_NOT_FOUND("Admin not found!"),
    ADMINS_NOT_FOUND("No admins found!"),
    INCORRECT_PASSWORD("Incorrect password!"),
    USERNAME_EMAIL_EXISTS("Username or Email already exists!"),
    USER_NOT_LOGGED("User is not logged in!"),
    UNAUTHORIZED("Insufficient permissions!"),
    BLOG_NOT_FOUND("Blog not found!"),
    BLOGS_NOT_FOUND("No blogs found!"),
    ARTICLE_NOT_FOUND("Article not found!"),
    ARTICLES_NOT_FOUND("No articles found!");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
