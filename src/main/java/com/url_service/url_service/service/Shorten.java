package com.url_service.url_service.service;

import com.url_service.url_service.dto.AnalyticMessage;
import com.url_service.url_service.dto.ShortenResponseDto;
import com.url_service.url_service.dto.UniqueIDResponseDto;
import com.url_service.url_service.utils.URLUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
public class Shorten {
    private final URLUtils utils;
    private final WebClient webClient;
    private final RedisService redisService;
    private final RedisCacheService redisCacheService;
    private final KafkaService kafkaService;


    public Shorten(URLUtils utils, WebClient webClient, RedisService redisService, RedisCacheService redisCacheService, KafkaService kafkaService) {
        this.utils = utils;
        this.webClient = webClient;
        this.redisService = redisService;
        this.redisCacheService = redisCacheService;
        this.kafkaService = kafkaService;
    }

    public ShortenResponseDto GiveEncodedUrl(String url) {
        try {
            UniqueIDResponseDto response = webClient.get()
                    .uri("/randomID")
                    .retrieve()
                    .bodyToMono(UniqueIDResponseDto.class).block();
            assert response != null;
            System.out.println(response.getRandomID());
            if (response.getRandomID() == -1) {
                return ShortenResponseDto.builder()
                        .encodeurl("")
                        .url(url)
                        .status("FAILED")
                        .build();
            }
            String res = utils.IDToURLGenerator(response.getRandomID());

            redisService.setValue(res, url);
            redisCacheService.setCache(res, url);

            return ShortenResponseDto.builder().url(url).encodeurl(res).status("Success").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String GiveUrl(String code, String ip) {
        if (redisCacheService.getCache(code) != null) {
            String res = redisCacheService.getCache(code);
            System.out.println("Cache Hit " + res);
            kafkaService.sendMessage(AnalyticMessage.builder()
                    .short_code(code)
                    .ipv4(ip)
                    .timestamp(Instant.now())
                    .originalUrl(res)
                    .browser("Chrome")
                    .referer("test")
                    .build());
            return res;
        }
        if (redisService.exists(code)) {
            String res = redisService.getValue(code);
            System.out.println(res);
            kafkaService.sendMessage(AnalyticMessage.builder()
                    .short_code(code)
                    .ipv4(ip)
                    .timestamp(Instant.now())
                    .eventDate(Instant.now())
                    .originalUrl(res)
                    .browser("Chrome")
                    .referer("test")
                    .build());
            return res;
        }
        return "URL Not Found";
    }
}
