package com.github.vitaliibaranetskyi.socialbird.controller

import com.github.vitaliibaranetskyi.socialbird.service.AuthService
import com.github.vitaliibaranetskyi.socialbird.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    private final AuthService authService

    AuthController(AuthService authService) {
        this.authService = authService
    }

    @GetMapping("/login")
    ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password)
        String token = authService.loginUser(authentication)
        ResponseEntity.ok(token)
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "")
        authService.logoutUser(token)
        ResponseEntity.noContent().build()
    }
}
