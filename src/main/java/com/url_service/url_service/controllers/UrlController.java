package com.url_service.url_service.controllers;

import com.url_service.url_service.dto.GetUrl;
import com.url_service.url_service.dto.GiveUrlDto;
import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.service.Shorten;
import com.url_service.url_service.dto.ShortenResponseDto;
import java.net.URI;
import java.util.Objects;

import com.url_service.url_service.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/url")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UrlController {
    private final Shorten shorten;


    public UrlController(Shorten shorten) {
        this.shorten = shorten;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody GetUrl url) {
        // Delegate to service to create shortened URL
        ShortenResponseDto response = shorten.GiveEncodedUrl(url.getOriginalUrl());

        if (response == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // If creation succeeded, return 201 Created with Location header
        if ("Success".equalsIgnoreCase(response.getStatus()) && response.getEncodeurl() != null && !response.getEncodeurl().isEmpty()) {
            URI location = URI.create("/url/give/" + response.getEncodeurl());
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }

        // Map known failure case (e.g. unauthenticated user) to 401 Unauthorized
        if (response.getStatus() != null && "FAILED".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Other failures -> 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/give/{code}")
    public ResponseEntity<?> GetShortenUrl(@PathVariable String code, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip == null) {
            System.out.println("ip problem");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        GiveUrlDto result = shorten.GiveUrl(code,ip);
        if(result!=null){
            if(Objects.equals(result.getMessage(), "FAILED")){
                return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(result, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/whoami")
    public String whoami(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        CustomUserDetails userDetails = AuthUtils.getAuthenticatedUser();
        String email = userDetails != null ? userDetails.getEmail() : "anonymous";
        String id = userDetails != null && userDetails.getId() != null ? String.valueOf(userDetails.getId()) : "-";
        return "User: " + email + " | ID: " + id + " | IP: " + ip;
    }
}