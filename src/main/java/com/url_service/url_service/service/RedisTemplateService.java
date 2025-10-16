package com.url_service.url_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisTemplateService {
    private final StringRedisTemplate stringRedisTemplate;

    public void setValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithExpiry(String key, String value, long seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }
}