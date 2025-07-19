package com.example.redisTest.controller;

import com.example.redisTest.ArticleCacheDto;
import com.example.redisTest.RequestDto;
import com.example.redisTest.entity.Article;
import com.example.redisTest.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 4. REST 컨트롤러
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService service;

    // 전체 목록 (캐시 적용 안 함)
    @GetMapping
    public List<Article> list() {
        return service.repo.findAll();
    }

    // 단건 조회 (캐시 @Cacheable)
    @GetMapping("/{id}")
    public ArticleCacheDto get(@PathVariable Long id) {

        long start = System.currentTimeMillis();
        ArticleCacheDto articleCacheDto = service.getById(id);
        long end = System.currentTimeMillis();
        log.info("time={}", end - start);
        return articleCacheDto;
    }

    // 생성 (캐시 @CachePut)
    @PostMapping
    public ArticleCacheDto create(@RequestBody RequestDto article) {
        return service.create(article);
    }

    // 수정 (캐시 @CachePut)
    @PutMapping("/{id}")
    public Article update(@PathVariable Long id,
                          @RequestBody Article article) {
        article.setId(id);
        return service.update(article);
    }

    // 삭제 (캐시 @CacheEvict)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
