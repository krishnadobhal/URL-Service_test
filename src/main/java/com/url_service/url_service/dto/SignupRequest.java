package com.url_service.url_service.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
}
