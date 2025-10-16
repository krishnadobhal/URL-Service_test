package com.url_service.url_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortenResponseDto {
    private String encodeurl;
    private String url;
    private String status;
}
