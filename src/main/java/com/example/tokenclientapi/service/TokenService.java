package com.example.tokenclientapi.service;

import com.example.tokenclientapi.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class TokenService {

    private final SecretKey secretKey;

    public TokenService(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractClientId(String token) {
        try {
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(cleanToken)
                    .getPayload();

            String clientId = claims.get("clientId", String.class);

            if (clientId == null || clientId.isBlank()) {
                throw new InvalidTokenException("Token nao contem o claim 'clientId'");
            }

            return clientId;
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("Token expirado", ex);
        } catch (JwtException ex) {
            throw new InvalidTokenException("Token invalido: " + ex.getMessage(), ex);
        }
    }
}
