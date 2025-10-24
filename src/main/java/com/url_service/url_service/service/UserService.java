package com.url_service.url_service.service;

import com.url_service.url_service.models.User;
import com.url_service.url_service.repository.UserRepository;
import com.url_service.url_service.security.CustomUserDetails;
import com.url_service.url_service.utils.AuthUtils;
import org.springframework.stereotype.Service;

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
}
