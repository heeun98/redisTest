package com.example.redisTest.service;

import com.example.redisTest.ArticleCacheDto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;


    @Test
    public void redis_test() {

        long start = System.currentTimeMillis();
        ArticleCacheDto byId = articleService.getById(2L);
        long end = System.currentTimeMillis();

    }


    @Test
    public void No_redis_test() {

        long start = System.currentTimeMillis();
        ArticleCacheDto byId = articleService.getById2(2L);
        long end = System.currentTimeMillis();

    }



}