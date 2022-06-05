package com.spring.Blog.model;

import com.spring.Blog.utility.user.UserRoles;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean logged;
    @Enumerated(EnumType.ORDINAL)
    private UserRoles role;

    @OneToMany(mappedBy = "owner")
    private List<Blog> blogs = new ArrayList<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.logged = false;
       // this.role = UserRoles.USER;
    }
}

