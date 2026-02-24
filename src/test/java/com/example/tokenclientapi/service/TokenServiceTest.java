package com.example.tokenclientapi.service;

import com.example.tokenclientapi.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private static final String SECRET = "c2VjcmV0LWtleS1mb3ItdGVzdGluZy1wdXJwb3Nlcy1vbmx5LW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHM=";
    private SecretKey secretKey;
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(SECRET);
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(String clientId, Date expiration) {
        var builder = Jwts.builder()
                .claim("clientId", clientId)
                .issuedAt(new Date());
        if (expiration != null) {
            builder.expiration(expiration);
        }
        return builder.signWith(secretKey).compact();
    }

    @Test
    @DisplayName("Deve extrair clientId de um token valido")
    void shouldExtractClientIdFromValidToken() {
        String token = generateToken("client-001", Date.from(Instant.now().plusSeconds(3600)));

        String clientId = tokenService.extractClientId(token);

        assertEquals("client-001", clientId);
    }

    @Test
    @DisplayName("Deve extrair clientId de token com prefixo Bearer")
    void shouldExtractClientIdFromTokenWithBearerPrefix() {
        String token = "Bearer " + generateToken("client-002", Date.from(Instant.now().plusSeconds(3600)));

        String clientId = tokenService.extractClientId(token);

        assertEquals("client-002", clientId);
    }

    @Test
    @DisplayName("Deve lancar InvalidTokenException para token expirado")
    void shouldThrowExceptionForExpiredToken() {
        String token = generateToken("client-001", Date.from(Instant.now().minusSeconds(3600)));

        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.extractClientId(token)
        );

        assertEquals("Token expirado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar InvalidTokenException para token invalido")
    void shouldThrowExceptionForInvalidToken() {
        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.extractClientId("token.invalido.aqui")
        );

        assertTrue(exception.getMessage().startsWith("Token invalido:"));
    }

    @Test
    @DisplayName("Deve lancar InvalidTokenException quando clientId esta ausente no token")
    void shouldThrowExceptionWhenClientIdIsMissing() {
        String token = Jwts.builder()
                .claim("otherClaim", "value")
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(secretKey)
                .compact();

        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.extractClientId(token)
        );

        assertEquals("Token nao contem o claim 'clientId'", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar InvalidTokenException quando clientId esta vazio no token")
    void shouldThrowExceptionWhenClientIdIsBlank() {
        String token = generateToken("   ", Date.from(Instant.now().plusSeconds(3600)));

        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.extractClientId(token)
        );

        assertEquals("Token nao contem o claim 'clientId'", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar InvalidTokenException para token assinado com chave diferente")
    void shouldThrowExceptionForTokenSignedWithDifferentKey() {
        SecretKey otherKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode("b3RoZXItc2VjcmV0LWtleS1mb3ItdGVzdGluZy1wdXJwb3Nlcy1vbmx5LW11c3QtYmUtYXQtbGVhc3QtMjU2")
        );

        String token = Jwts.builder()
                .claim("clientId", "client-001")
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(otherKey)
                .compact();

        assertThrows(InvalidTokenException.class, () -> tokenService.extractClientId(token));
    }
}
