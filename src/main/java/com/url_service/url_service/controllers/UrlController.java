package com.url_service.url_service.controllers;

import com.url_service.url_service.service.Shorten;
import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/url")
public class UrlController {
    private final Shorten shorten;


    public UrlController(Shorten shorten) {
        this.shorten = shorten;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody String url) {
        return new ResponseEntity<>(shorten.GiveEncodedUrl(url), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> GetShortenUrl(@PathVariable String code, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip == null) {
            System.out.println("ip problem");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String result = shorten.GiveUrl(code,ip);
        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/whoami")
    public String whoami(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return "User: " + userDetails.getEmail() + " | ID: " + userDetails.getId() + " | IP: " + ip;
    }
}