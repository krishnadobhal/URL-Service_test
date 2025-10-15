package com.url_service.url_service.controllers;

import com.url_service.url_service.service.Shorten;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new  ResponseEntity<>(shorten.GiveEncodedUrl(url), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> GetShortenUrl(@PathVariable String code, HttpServletRequest request) {

        String ip=request.getRemoteAddr();
        if(ip==null){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new  ResponseEntity<>(shorten.GiveUrl(code,ip), HttpStatus.ACCEPTED);
    }
}
