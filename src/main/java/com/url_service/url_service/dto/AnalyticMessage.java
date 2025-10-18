package com.url_service.url_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class AnalyticMessage {
    private String short_code;
    private String originalUrl;
    private Instant timestamp;
    private String ipv4;
    private String userAgent;
    private String browser;
    private String referer;
    private String osName;
    private Instant eventDate;
}
