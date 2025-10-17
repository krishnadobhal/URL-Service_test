package com.url_service.url_service.controllers;

import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.service.Shorten;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
public class UrlController{
    private final Shorten shorten;


    public UrlController(Shorten shorten) {
        this.shorten = shorten;
    }
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody String url) {
        // Safely get authenticated user if present
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        Long id = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            email = userDetails.getEmail();
            id = userDetails.getId();
        }

        // ...use email and id as needed (may be null for anonymous requests)...

        return new  ResponseEntity<>(shorten.GiveEncodedUrl(url), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> GetShortenUrl(@PathVariable String code, HttpServletRequest request) {
        // Safely get authenticated user if present
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        Long id = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            email = userDetails.getEmail();
            id = userDetails.getId();
        }

        // ...use email and id as needed (may be null for anonymous requests)...

        String ip=request.getRemoteAddr();
        if(ip==null){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println(code);
        return new  ResponseEntity<>(shorten.GiveUrl(code), HttpStatus.ACCEPTED);
    }
}