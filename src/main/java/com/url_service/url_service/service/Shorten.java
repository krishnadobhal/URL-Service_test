package com.url_service.url_service.service;

import com.url_service.url_service.dto.AnalyticMessage;
import com.url_service.url_service.dto.ShortenResponseDto;
import com.url_service.url_service.dto.UniqueIDResponseDto;
import com.url_service.url_service.utils.URLUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
public class Shorten {
    private final URLUtils utils;
    private final WebClient webClient;
    private final KafkaService kafkaService;
    private final RedisTemplateService redisTemplateService;


    public Shorten(URLUtils utils, WebClient webClient, KafkaService kafkaService, RedisTemplateService redisTemplateService) {
        this.utils = utils;
        this.webClient = webClient;
        this.kafkaService = kafkaService;
        this.redisTemplateService = redisTemplateService;
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

//            redisService.setValue(res, url);
            redisTemplateService.setValue(res,url);
//            kafkaService.sendMessage(AnalyticMessage.builder().url(res).build());

            return ShortenResponseDto.builder().url(url).encodeurl(res).status("Success").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Cacheable(value = "urls", key = "#code")
    public String getUrlInternal(String code) {
        return redisTemplateService.getValue(code);
    }

    public String GiveUrl(String code) {
        String url = getUrlInternal(code);
        if (url != null) {
            kafkaService.sendMessage(AnalyticMessage.builder()
                    .shortCode(code)
//                            .userAgent(userRepository.findById())
                    .originalUrl("http://12")
                    .timestamp(Instant.now())
                    .ipv4("0.0.0.0")
                    .build());

        }
        return url;
    }
}