package com.url_service.url_service.service;

import com.url_service.url_service.dto.AnalyticMessage;
import com.url_service.url_service.dto.ShortenResponseDto;
import com.url_service.url_service.dto.UniqueIDResponseDto;
import com.url_service.url_service.models.Url;
import com.url_service.url_service.models.User;
import com.url_service.url_service.repository.UrlRepository;
import com.url_service.url_service.repository.UserRepository;
import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.utils.AuthUtils;
import com.url_service.url_service.utils.URLUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class Shorten {
    private final URLUtils utils;
    private final WebClient webClient;
    private final KafkaService kafkaService;
    private final RedisTemplateService redisTemplateService;
    private final UserRepository userRepository;
    private final UrlRepository urlRepository;
    private final URLCacheService urlCacheService;


    public Shorten(URLUtils utils, WebClient webClient, KafkaService kafkaService, RedisTemplateService redisTemplateService, UserRepository userRepository, URLCacheService urlCacheService, UserService userService, UrlRepository urlRepository) {
        this.utils = utils;
        this.webClient = webClient;
        this.kafkaService = kafkaService;
        this.redisTemplateService = redisTemplateService;
        this.userRepository = userRepository;
        this.urlCacheService = urlCacheService;
        this.urlRepository = urlRepository;
    }

    public ShortenResponseDto GiveEncodedUrl(String url) {
        try {
            CustomUserDetails userDetails = AuthUtils.getAuthenticatedUser();
            if (userDetails == null) {
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
            String code = utils.IDToURLGenerator(response.getRandomID());

            //fetch user details
            Optional<User> user=userRepository.findByEmail(userDetails.getEmail());
            if(user.isEmpty()){
                return ShortenResponseDto.builder().url(url).encodeurl(code).status("Failed").build();
            }

            //Creating Url
            Url urlOBJ=Url.builder().originalUrl(url).shortCode(code).clickCount(0).user(user.get()).build();
            Url url1=urlRepository.save(urlOBJ);

            // populate both the raw Redis store and the Spring cache
            urlCacheService.putUrlInCache(code, url);

            return ShortenResponseDto.builder().url(url).encodeurl(code).status("Success").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String GiveUrl(String code, String ip) {
        String url = urlCacheService.getUrlFromCache(code);
        urlCacheService.putUrlInCache(code, url);
        CustomUserDetails userDetails = AuthUtils.getAuthenticatedUser();
        if (userDetails == null) {
            return "User not authenticated";
        }
        System.out.println("User authenticated "+ userDetails.getEmail());
        String email = userDetails.getEmail();
        ZonedDateTime istTimestamp = Instant.now().atZone(ZoneId.of("Asia/Kolkata"));
        if (userRepository.findByEmail(email).isPresent()) {
            User user = userRepository.findByEmail(email).get();
            System.out.println(ip);
            if (url != null) {

                //Update Click Count
                Url url1= urlRepository.findByShortCode(code);
                urlRepository.updateClickCountByClickCount(url1.getId());

                //Send Kafka Message
                kafkaService.sendMessage(AnalyticMessage.builder()
                        .short_code(code)
                        .userAgent(user.getUsername())
                        .originalUrl(url)
                        .timestamp(istTimestamp.toInstant())
                        .ipv4(ip)
                        .build());
                // update both raw Redis and Spring cache (write-through)
                urlCacheService.putUrlInCache(code, url);
            } else {
                return "URL not found";
            }
        }
        return url;
    }
}