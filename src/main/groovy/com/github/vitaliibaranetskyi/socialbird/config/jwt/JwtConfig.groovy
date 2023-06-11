package com.github.vitaliibaranetskyi.socialbird.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.crypto.spec.SecretKeySpec
import java.security.Key

@Component
class JwtConfig {

    private List<String> invalidatedTokens = new ArrayList<>()

    @Value('${jwt.secret}')
    private String secret

    @Value('${jwt.expiration}')
    private int expiration

    String generateToken(String username) {
        Date now = new Date()
        Date expiryDate = new Date(now.getTime() + expiration)

        Key signingKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName())

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey)

        return builder.compact()
    }

    boolean validateToken(String token) {
        if (invalidatedTokens.contains(token)) {
            return false
        }
        try {
            Key signingKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName())
            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token)
            return true
        } catch (Exception ignored) {
            return false
        }
    }

    String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).body
        return claims.subject
    }

    void invalidateToken(String token) {
        invalidatedTokens.add(token)
    }
}
