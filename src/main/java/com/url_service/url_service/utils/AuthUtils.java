package com.url_service.url_service.utils;

import com.url_service.url_service.security.CustomUserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Lightweight replacement for SecurityContext-based lookup.
 * Reads subject set by the SimpleJwtFilter from the current HttpServletRequest
 * and returns a minimal CustomUserDetails (id left null, empty authorities).
 */
public class AuthUtils {
    public static CustomUserDetails getAuthenticatedUser() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest request = attrs.getRequest();
        if (request == null) return null;
        Object subj = request.getAttribute("jwt.subject");
        if (subj == null) return null;
        String email = String.valueOf(subj);
        return new CustomUserDetails(null, email, "", Collections.emptyList());
    }

}
