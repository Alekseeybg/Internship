package com.spring.Blog.utility.exception;

public enum ExceptionMessages {
    USER_NOT_FOUND("User not found!"),
    USERS_NOT_FOUND("No users found!"),
    ADMIN_NOT_FOUND("Admin not found!"),
    ADMINS_NOT_FOUND("No admins found!"),
    INCORRECT_PASSWORD("Incorrect password!"),
    INCORRECT_EMAIL("Wrong email"),
    USERNAME_EMAIL_EXISTS("Username or Email already exists!"),
    USER_NOT_LOGGED("User is not logged in!"),
    INVALID_BLOG_TITLE("The blog title must be between 3 and 255 characters long!"),
    BLOG_TITLE_EXISTS("The blog title already exists!"),
    UNAUTHORIZED("Insufficient permissions!"),
    BLOG_NOT_FOUND("Blog not found!"),
    BLOGS_NOT_FOUND("No blogs found!"),
    ARTICLE_NOT_FOUND("Article not found!"),
    ARTICLES_NOT_FOUND("No articles found!"),
    INVALID_ARTICLE_TITLE("The article title must be between 3 and 255 characters long!"),
    ARTICLE_TITLE_EXISTS("The article title already exists!"),
    INVALID_ARTICLE_CONTENT("The article content must be between 3 and 255 characters long!"),
    IMAGE_NOT_FOUND("Image not found!");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
