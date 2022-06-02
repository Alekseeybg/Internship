package com.spring.Blog.utility.user;

public enum UserRoles {
    USER("User"),
    ADMIN("Admin"),
    CREATOR("Creator"),
    EDITOR("Editor");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
