package com.example.redisTest.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.*;

@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    /**
     * 스프링 캐시 추상화(Cache Abstraction)**를 Redis backed 로 구현한 매니저
     *
     * @Cacheable / @CachePut / @CacheEvict 같은 애노테이션 기반 캐시 기능이
     * → 실제로 어디에, 어떻게 저장될지를 담당
     * 장점 :
     * 비즈니스 로직에 캐시 관련 코드를 전혀 안 넣어도, 애노테이션만 달면 자동으로 저장·조회·삭제
     * TTL, 직렬화, 네임스페이스 같은 설정을 중앙에서 관리
     *----------------------------------------------------------
     * 언제 쓰나?
     * 조회 메서드 결과를 “자동으로” 캐싱하고 싶을 때
     * “캐시 추상화” 레벨에서 작업을 끝내고 싶은 경우
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // 1) JSON 직렬화를 위한 Serializer 준비
        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer();
        // 2) 캐시 설정: TTL 60초, 키는 String, 값은 JSON
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(6000))                                // TTL 60초
                .serializeKeysWith(
                        fromSerializer(new StringRedisSerializer())
                )                                                                // 키는 String
                .serializeValuesWith(
                        fromSerializer(jsonSerializer)
                )                                                                // 값은 JSON
                // (선택) null 값 캐싱 방지
                .disableCachingNullValues();
        // 3) CacheManager 빌더에 설정 적용
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .build();

    }
}
