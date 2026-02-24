package com.example.tokenclientapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MockClientController.class)
class MockClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar cliente mock existente - client-001")
    void shouldReturnExistingMockClient001() throws Exception {
        mockMvc.perform(get("/mock/clients/client-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("client-001"))
                .andExpect(jsonPath("$.name").value("Joao Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    @DisplayName("Deve retornar cliente mock existente - client-002")
    void shouldReturnExistingMockClient002() throws Exception {
        mockMvc.perform(get("/mock/clients/client-002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("client-002"))
                .andExpect(jsonPath("$.name").value("Maria Souza"))
                .andExpect(jsonPath("$.email").value("maria.souza@email.com"))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    @DisplayName("Deve retornar cliente mock inativo - client-003")
    void shouldReturnInactiveMockClient003() throws Exception {
        mockMvc.perform(get("/mock/clients/client-003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("client-003"))
                .andExpect(jsonPath("$.name").value("Pedro Santos"))
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    @DisplayName("Deve retornar 404 para cliente mock inexistente")
    void shouldReturn404ForNonExistentClient() throws Exception {
        mockMvc.perform(get("/mock/clients/client-999"))
                .andExpect(status().isNotFound());
    }
}
