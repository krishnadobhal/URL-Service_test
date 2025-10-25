package com.url_service.url_service.controllers;


import com.url_service.url_service.dto.GetUserUrlDto;
import com.url_service.url_service.models.User;
import com.url_service.url_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    @GetMapping("/urls")
    public ResponseEntity<?> getUrls(){
        GetUserUrlDto GetUserUrls=userService.GetUserUrls();
        return new ResponseEntity<>(GetUserUrls,HttpStatus.OK);
    }
}
