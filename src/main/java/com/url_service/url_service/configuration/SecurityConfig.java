package com.url_service.url_service.configuration;

import com.url_service.url_service.filters.SimpleJwtFilter;
import com.url_service.url_service.security.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    // Register a simple servlet filter that only validates JWT signature and expiration
    // The filter sets a request attribute "jwt.subject" when a valid token is present.
    @Bean
    public FilterRegistrationBean<SimpleJwtFilter> jwtFilterRegistration(JwtUtil jwtUtil) {
        FilterRegistrationBean<SimpleJwtFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new SimpleJwtFilter(jwtUtil));
        reg.addUrlPatterns("/*");
        reg.setOrder(1);
        return reg;
    }
}
