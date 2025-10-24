package com.url_service.url_service.filters;

import com.url_service.url_service.security.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Simple servlet filter that validates JWT signature and expiration using JwtUtil.
 * If the token is valid, it sets the request attribute "jwt.subject" to the token subject.
 */
public class SimpleJwtFilter implements Filter {
    private final JwtUtil jwtUtil;

    public SimpleJwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest http = (HttpServletRequest) request;
            String header = http.getHeader("Authorization");
            if (header != null && header.toLowerCase().startsWith("bearer ")) {
                String token = header.substring(7).trim();
                if (!token.isEmpty() && !token.contains(" ") && jwtUtil.validateToken(token)) {
                    String subject = jwtUtil.getSubject(token);
                    if (subject != null) {
                        http.setAttribute("jwt.subject", subject);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}
