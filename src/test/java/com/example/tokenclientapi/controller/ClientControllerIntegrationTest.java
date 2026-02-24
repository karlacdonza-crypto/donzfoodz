package com.example.tokenclientapi.controller;

import com.example.tokenclientapi.dto.ClientResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ClientControllerIntegrationTest {

    private static final String TEST_SECRET = "c2VjcmV0LWtleS1mb3ItdGVzdGluZy1wdXJwb3Nlcy1vbmx5LW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHM=";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private String generateToken(String clientId) {
        byte[] keyBytes = Base64.getDecoder().decode(TEST_SECRET);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .claim("clientId", clientId)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(key)
                .compact();
    }

    private String generateExpiredToken(String clientId) {
        byte[] keyBytes = Base64.getDecoder().decode(TEST_SECRET);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .claim("clientId", clientId)
                .issuedAt(Date.from(Instant.now().minusSeconds(7200)))
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(key)
                .compact();
    }

    private HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    @Test
    @DisplayName("Fluxo completo: token valido com client-001 deve retornar dados do cliente")
    void shouldReturnClientDataForValidToken() {
        String token = generateToken("client-001");

        ResponseEntity<ClientResponse> response = restTemplate.exchange(
                baseUrl() + "/api/clients",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                ClientResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("client-001", response.getBody().clientId());
        assertEquals("Joao Silva", response.getBody().name());
        assertEquals("joao.silva@email.com", response.getBody().email());
        assertEquals("ATIVO", response.getBody().status());
    }

    @Test
    @DisplayName("Fluxo completo: token valido com client-002 deve retornar dados do cliente")
    void shouldReturnClientDataForClient002() {
        String token = generateToken("client-002");

        ResponseEntity<ClientResponse> response = restTemplate.exchange(
                baseUrl() + "/api/clients",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                ClientResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("client-002", response.getBody().clientId());
        assertEquals("Maria Souza", response.getBody().name());
        assertEquals("maria.souza@email.com", response.getBody().email());
        assertEquals("ATIVO", response.getBody().status());
    }

    @Test
    @DisplayName("Fluxo completo: token valido com client-003 inativo deve retornar dados")
    void shouldReturnInactiveClientData() {
        String token = generateToken("client-003");

        ResponseEntity<ClientResponse> response = restTemplate.exchange(
                baseUrl() + "/api/clients",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                ClientResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("client-003", response.getBody().clientId());
        assertEquals("Pedro Santos", response.getBody().name());
        assertEquals("INATIVO", response.getBody().status());
    }

    @Test
    @DisplayName("Deve retornar 401 para token expirado")
    void shouldReturn401ForExpiredToken() {
        String token = generateExpiredToken("client-001");

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/api/clients",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().contains("Token expirado"));
    }

    @Test
    @DisplayName("Deve retornar 401 para token invalido")
    void shouldReturn401ForInvalidToken() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/api/clients",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders("token.invalido.aqui")),
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar erro quando clientId do token nao existe no mock")
    void shouldReturnErrorForNonExistentClientId() {
        String token = generateToken("client-999");

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/api/clients",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Cliente nao encontrado com clientId: client-999"));
    }
}
