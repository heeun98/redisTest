package com.example.redisTest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostLikesDto {
    Long postId;
    Long likesCount;
}