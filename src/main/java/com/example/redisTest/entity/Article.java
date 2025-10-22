package com.example.redisTest.entity;

import jakarta.persistence.*;
import lombok.*;

// 1. Article 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 2000)
    private String body;
    private Long likes;

    public Article(String title, String body) {
        this.title = title;
        this.body = body;
    }


}
