package com.example.tokenclientapi.controller;

import com.example.tokenclientapi.dto.ClientResponse;
import com.example.tokenclientapi.service.ClientService;
import com.example.tokenclientapi.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final TokenService tokenService;
    private final ClientService clientService;

    public ClientController(TokenService tokenService, ClientService clientService) {
        this.tokenService = tokenService;
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<ClientResponse> getClient(
            @RequestHeader("Authorization") String authorizationHeader) {

        String clientId = tokenService.extractClientId(authorizationHeader);
        ClientResponse client = clientService.getClientByClientId(clientId);

        return ResponseEntity.ok(client);
    }
}
