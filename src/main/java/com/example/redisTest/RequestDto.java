package com.example.redisTest;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class RequestDto {
    private Long id;
    private String title;
    private String body;
}
