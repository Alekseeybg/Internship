package com.spring.Blog.utility.user;

public enum ValidationMessages {
    SUCCESS("Success"),
    NAME_IS_NOT_VALID("Username must be between 3 and 100 characters and may contain only alphanumeric characters and/or underscores"),
    PASSWORD_IS_NOT_VALID("Password must be at least 6 characters and must have at least one lowercase letter, one uppercase letter, one digit and one special character"),
    EMAIL_IS_NOT_VALID("Email is invalid");
    private final String message;

    ValidationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
