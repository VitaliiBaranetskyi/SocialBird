package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.config.jwt.JwtConfig
import com.github.vitaliibaranetskyi.socialbird.service.AuthService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager
    private final JwtConfig jwtConfig

    AuthServiceImpl(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager
        this.jwtConfig = jwtConfig
    }

    String loginUser(Authentication authentication) {
        Authentication authenticated = authenticationManager.authenticate(authentication)
        SecurityContextHolder.context.authentication = authenticated
        UserDetails userDetails = authenticated.principal as UserDetails
        jwtConfig.generateToken(userDetails.username)
    }

    void logoutUser(String token) {
        jwtConfig.invalidateToken(token)
        SecurityContextHolder.clearContext()
    }
}
