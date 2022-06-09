package com.spring.Blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties("owner")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "article_id")
    private List<Article> articles = new ArrayList<>();

    public Blog(String title) {
        this.title = title;
    }

    public void addArticle(Article article) {
        this.articles.add(article);
    }
}
