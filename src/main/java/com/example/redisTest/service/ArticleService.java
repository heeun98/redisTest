package com.example.redisTest.service;

import com.example.redisTest.ArticleCacheDto;
import com.example.redisTest.RequestDto;
import com.example.redisTest.entity.Article;
import com.example.redisTest.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "article")
public class ArticleService {

    public final ArticleRepository repo;


    @Transactional(readOnly = true)
    @Cacheable(key = "'all'")
    public List<ArticleCacheDto> findAll() {
        return repo.findAll()
                .stream()
                .map(a -> new ArticleCacheDto(a.getId(), a.getTitle(), a.getBody()))
                .collect(Collectors.toList());
    }

    /** 조회: @Cacheable → 캐시에 있으면 캐시에서 가져오고 없으면 DB에서 가져옴과 동시에 캐시에 올려둔다.(만료시간) */
    @Transactional
    @Cacheable(key = "#id")
    public ArticleCacheDto getById(Long id) {

        long start = System.currentTimeMillis();
        Article article = repo.findById(id)
                .orElseThrow(() -> new RuntimeException());

        return new ArticleCacheDto(article.getId(), article.getTitle(), article.getBody());

    }

    @Transactional
    public ArticleCacheDto getById2(Long id) {

        long start = System.currentTimeMillis();
        Article article = repo.findById(id)
                .orElseThrow(() -> new RuntimeException());

        return new ArticleCacheDto(article.getId(), article.getTitle(), article.getBody());

    }

    /** DB 반영: 메서드 로직(repo.save())으로 처리
     캐시 반영: 메서드 반환값을 무조건 캐시에 저장 */
    @Transactional
    @CachePut(key = "#result.id")
    @CacheEvict(key = "'all'") // -> redis-cli : DEL article::all
    public ArticleCacheDto create(RequestDto articleDto) {

        Article article = new Article(articleDto.getTitle(), articleDto.getBody());
        repo.save(article);

        return new ArticleCacheDto(article.getId(), article.getTitle(), article.getBody());
    }


    @Transactional
    @CachePut(key = "#articleDto.id")
    @CacheEvict(key = "'all'")
    public ArticleCacheDto update(RequestDto articleDto) {
        Article article = repo.findById(articleDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("not found"));
        article.setTitle(articleDto.getTitle());
        article.setBody(articleDto.getBody());
        // 변경 감지 → save 호출 안 해도 됨
        return new ArticleCacheDto(article.getId(), article.getTitle(), article.getBody());
    }

    /** 삭제: @CacheEvict → DB 삭제 후 캐시에서 해당 키 제거 */
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id"),       // article::{id} 삭제
            @CacheEvict(key = "'all'")      // article::all 삭제
    })
    public void delete(Long id) {
        repo.deleteById(id);
    }


}
