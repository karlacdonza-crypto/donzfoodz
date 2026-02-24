package com.example.tokenclientapi.dto;

public record ClientResponse(
        String clientId,
        String name,
        String email,
        String status
) {
}
