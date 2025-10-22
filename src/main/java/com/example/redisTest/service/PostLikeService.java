package com.example.redisTest.service;

import com.example.redisTest.PostLikesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostLikeService {

    private final StringRedisTemplate redisTemplate;
    private final String key = "post::";

    public PostLikesDto addLikes(Long postId, Long userId) {

        String key = buildKey(postId);

        // post::postId Set 에 userId 가 존재하는지
        // sismember post::postId userId
        Boolean isMember = redisTemplate.opsForSet().isMember(key, String.valueOf(userId));

        //존재안하면
        // sadd key String.value(userId)
        redisTemplate.opsForSet().add(key, String.valueOf(userId));


        //현재 좋아요 개수
        // scard key
        Long likesSize = redisTemplate.opsForSet().size(key);

        return new PostLikesDto(postId, likesSize);


    }

    public String buildKey(Long postId) {
        return key + postId;
    }



}
