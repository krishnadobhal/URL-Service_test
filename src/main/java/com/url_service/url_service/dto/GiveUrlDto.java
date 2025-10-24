package com.url_service.url_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class GiveUrlDto {
    public String url;
    public String status;
    public String message;
}
