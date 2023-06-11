package com.github.vitaliibaranetskyi.socialbird.config.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.util.StringUtils

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtConfig jwtConfig

    TokenAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        super(authenticationManager)
        this.jwtConfig = jwtConfig
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest(request)

        if (StringUtils.hasText(token) && jwtConfig.validateToken(token)) {
            String username = jwtConfig.getUsernameFromToken(token)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList())

            SecurityContextHolder.getContext().setAuthentication(authenticationToken)
        }

        filterChain.doFilter(request, response)
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}

