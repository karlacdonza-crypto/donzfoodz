package com.example.tokenclientapi.controller;

import com.example.tokenclientapi.dto.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock controller that simulates an external client service.
 * In production, this would be replaced by a real external API call.
 */
@RestController
@RequestMapping("/mock/clients")
public class MockClientController {

    private static final Map<String, ClientResponse> MOCK_CLIENTS = new ConcurrentHashMap<>();

    static {
        MOCK_CLIENTS.put("client-001", new ClientResponse(
                "client-001", "Joao Silva", "joao.silva@email.com", "ATIVO"));
        MOCK_CLIENTS.put("client-002", new ClientResponse(
                "client-002", "Maria Souza", "maria.souza@email.com", "ATIVO"));
        MOCK_CLIENTS.put("client-003", new ClientResponse(
                "client-003", "Pedro Santos", "pedro.santos@email.com", "INATIVO"));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable String clientId) {
        ClientResponse client = MOCK_CLIENTS.get(clientId);

        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(client);
    }
}
