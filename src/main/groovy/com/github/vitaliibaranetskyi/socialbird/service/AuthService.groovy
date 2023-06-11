package com.github.vitaliibaranetskyi.socialbird.service

import org.springframework.security.core.Authentication

interface AuthService {
    String loginUser(Authentication authentication)
    void logoutUser(String token)
}