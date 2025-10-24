package com.url_service.url_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class URLCacheService {
    private static final Logger log = LoggerFactory.getLogger(URLCacheService.class);

    private final RedisTemplateService redisTemplateService;

    public URLCacheService(RedisTemplateService redisTemplateService) {
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
}