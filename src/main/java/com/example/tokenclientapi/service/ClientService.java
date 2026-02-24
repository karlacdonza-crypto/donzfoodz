package com.example.tokenclientapi.service;

import com.example.tokenclientapi.dto.ClientResponse;
import com.example.tokenclientapi.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ClientService {

    private final WebClient webClient;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ClientResponse getClientByClientId(String clientId) {
        try {
            return webClient.get()
                    .uri("/{clientId}", clientId)
                    .retrieve()
                    .bodyToMono(ClientResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new ClientNotFoundException("Cliente nao encontrado com clientId: " + clientId);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao consultar servico mock: " + ex.getMessage(), ex);
        }
    }
}
