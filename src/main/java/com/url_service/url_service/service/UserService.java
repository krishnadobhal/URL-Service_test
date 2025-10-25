package com.url_service.url_service.service;

import com.url_service.url_service.dto.GetUserUrlDto;
import com.url_service.url_service.models.User;
import com.url_service.url_service.repository.UserRepository;
import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.utils.AuthUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository UserRepository;

    public UserService(UserRepository userRepository) {
        UserRepository = userRepository;
    }

    public User UpdateUsername(String username) {
        CustomUserDetails userDetails = AuthUtils.getAuthenticatedUser();
        if (userDetails!=null) {
            Long userID = userDetails.getId();
            UserRepository.updateUsername(username,userID);
            return UserRepository.findById(userID).get();
        }
        return null;
    }

    public GetUserUrlDto GetUserUrls(){
        CustomUserDetails userDetails = AuthUtils.getAuthenticatedUser();
        if (userDetails!=null) {
            if(UserRepository.findByEmail(userDetails.getEmail()).isPresent()){
                User user = UserRepository.findByEmail(userDetails.getEmail()).get();
                List<GetUserUrlDto.UrlDto> urlDtos = user.getUrls().stream()
                        .map(url -> GetUserUrlDto.UrlDto.builder()
                                .shortCode(url.getShortCode())
                                .originalUrl(url.getOriginalUrl())
                                .clickCount(url.getClickCount())
                                .build())
                        .toList();

                return GetUserUrlDto.builder()
                        .email(user.getEmail())
                        .status("success")
                        .message("User URLs retrieved successfully")
                        .urls(urlDtos)
                        .build();
            }
        }
        return null;
    }
}
