package com.spring.Blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import static org.hibernate.annotations.CascadeType.*;


import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"blog", "author","article_id"})
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String title;

    private String content;

    @ManyToOne
    @Cascade(SAVE_UPDATE)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
}