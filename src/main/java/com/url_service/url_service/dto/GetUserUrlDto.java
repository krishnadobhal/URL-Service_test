package com.url_service.url_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class GetUserUrlDto {
    private String email;
    private String status;
    private String message;
    private List<UrlDto > urls;
    @Getter
    @Setter
    @Builder
    public static class UrlDto {
        private String shortCode;
        private String originalUrl;
        private Integer clickCount;
    }
}
