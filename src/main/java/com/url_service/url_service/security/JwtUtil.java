package com.url_service.url_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret) {
        // secret is expected to be base64 encoded; decode via Keys.hmacShaKeyFor
        this.key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(base64Secret));
    }

    public boolean validateToken(String token) {
        try {
            if (token == null) return false;
            String t = token.trim();
            // allow Authorization header values like "Bearer <token>"
            if (t.toLowerCase().startsWith("bearer ")) {
                t = t.substring(7).trim();
            }
            // If token contains whitespace it's invalid (could trigger base64url decoding issues)
            if (t.isEmpty() || t.contains(" ")) return false;
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(t);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        if (token == null) return null;
        String t = token.trim();
        if (t.toLowerCase().startsWith("bearer ")) {
            t = t.substring(7).trim();
        }
        if (t.isEmpty() || t.contains(" ")) return null;
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(t).getBody();
        return claims.getSubject();
    }
}
