package com.url_service.url_service.service;

import com.url_service.url_service.dto.AnalyticMessage;
import com.url_service.url_service.dto.ShortenResponseDto;
import com.url_service.url_service.dto.UniqueIDResponseDto;
import com.url_service.url_service.models.User;
import com.url_service.url_service.repository.UserRepository;
import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.utils.AuthUtils;
import com.url_service.url_service.utils.URLUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class Shorten {
    private final URLUtils utils;
    private final WebClient webClient;
    private final KafkaService kafkaService;
    private final RedisTemplateService redisTemplateService;
    private final UserRepository UserRepository;
    private final URLCacheService urlCacheService;


    public Shorten(URLUtils utils, WebClient webClient, KafkaService kafkaService, RedisTemplateService redisTemplateService, UserRepository userRepository, URLCacheService urlCacheService) {
        this.utils = utils;
        this.webClient = webClient;
        this.kafkaService = kafkaService;
        this.redisTemplateService = redisTemplateService;
        UserRepository = userRepository;
        this.urlCacheService = urlCacheService;
    }

    public ShortenResponseDto GiveEncodedUrl(String url) {
        try {
            Authentication isAuthenticated = AuthUtils.getAuthenticatedUser();
            if(isAuthenticated==null){
                return ShortenResponseDto.builder()
                        .encodeurl("")
                        .url(url)
                        .status("FAILED")
                        .build();
            }
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
            redisTemplateService.setValue(res, url);
            kafkaService.sendMessage(AnalyticMessage.builder().originalUrl(res).build());

            return ShortenResponseDto.builder().url(url).encodeurl(res).status("Success").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String GiveUrl(String code,String ip) {
        String url = urlCacheService.getUrlFromCache(code);
        Authentication isAuthenticated = AuthUtils.getAuthenticatedUser();
        if(isAuthenticated==null){
            return "User not authenticated";
        }
        CustomUserDetails userDetails = (CustomUserDetails) isAuthenticated.getPrincipal();
        Long id = userDetails.getId();
        ZonedDateTime istTimestamp = Instant.now().atZone(ZoneId.of("Asia/Kolkata"));
        if (UserRepository.findById(id).isPresent()) {
            User user = UserRepository.findById(id).get();
            System.out.println(ip);
            if (url != null) {
                kafkaService.sendMessage(AnalyticMessage.builder()
                        .short_code(code)
                        .userAgent(user.getUsername())
                        .originalUrl(url)
                        .timestamp(istTimestamp.toInstant())
                        .ipv4(ip)
                        .build());
            } else {
                return "URL not found";
            }
        }
        return url;
    }
}