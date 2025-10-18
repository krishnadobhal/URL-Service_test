package com.url_service.url_service.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class URLCacheService {
    private final RedisTemplateService redisTemplateService;

    public URLCacheService(RedisTemplateService redisTemplateService) {
        this.redisTemplateService = redisTemplateService;
    }

    @Cacheable(value = "urls", key = "#code")
    public String getUrlFromCache(String code) {
        return redisTemplateService.getValue(code);
    }
}