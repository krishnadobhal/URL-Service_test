package com.url_service.url_service.controllers;


import com.url_service.url_service.models.User;
import com.url_service.url_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/username")
    public ResponseEntity<?> shortenUrl(@RequestBody String username) {
        User user=userService.UpdateUsername(username);
        if(user==null){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new  ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }
}
