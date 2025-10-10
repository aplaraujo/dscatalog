package com.example.dscatalog.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtils {
    public static String getAuthenticatedUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            return jwtPrincipal.getClaim("username");
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }
}
