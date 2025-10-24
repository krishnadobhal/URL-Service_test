package com.url_service.url_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    private final RedisTemplateService redisTemplateService;

    public CacheService(RedisTemplateService redisTemplateService) {
        this.redisTemplateService = redisTemplateService;
    }

    @Cacheable(value = "urls", key = "#code")
    public String getUrlFromCache(String code) {
        System.out.println("CACHE MISS - getUrlFromCache code=" + code);
        log.debug("Cache MISS - executing getUrlFromCache for code={}", code);
        return redisTemplateService.getValue(code);
    }

    @CachePut(value = "urls", key = "#code")
    public String putUrlInCache(String code, String url) {
        log.debug("Putting value into cache and raw Redis for code={}", code);
        if (url != null) {
            redisTemplateService.setValue(code, url);
        }
        return url;
    }
    @Cacheable(value = "user", key = "#code")
    public String getUserFromCache(String code) {
        System.out.println("CACHE MISS - getUserFromCache code=" + code);
        log.debug("Cache MISS - executing getUserFromCache for code={}", code);
        return redisTemplateService.getValue(code);
    }

    @CachePut(value = "user", key = "#code")
    public String putUserInCache(String code, String user) {
        log.debug("Putting value into cache and raw Redis for code={}", code);
        if (user != null) {
            redisTemplateService.setValue(code, user);
        }
        return user;
    }
}