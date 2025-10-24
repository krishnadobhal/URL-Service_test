package com.url_service.url_service.security;

import java.util.Collection;

/**
 * Lightweight user details holder used across the application.
 * This is intentionally a plain POJO so the project does not require Spring Security on the classpath.
 */
public class CustomUserDetails {
    private Long id;
    private String email;
    private String password;
    private Collection<String> authorities;

    public CustomUserDetails(Long id, String email, String password, Collection<String> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }

}
