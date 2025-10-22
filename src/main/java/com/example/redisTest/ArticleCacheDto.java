package com.example.redisTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCacheDto {

    private Long id;
    private String title;
    private String body;
}
