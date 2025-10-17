package com.url_service.url_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class LoginResponse {
    public Integer id;
    public String email;
    public LocalDateTime createdAt;
}
