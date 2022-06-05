package com.spring.Blog.utility.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Getter
public enum UserRoles {
    USER("User"),
    ADMIN("Admin"),
    CREATOR("Creator"),
    EDITOR("Editor");

    private final String role;

    public String getRole() {
        return role;
    }
}
