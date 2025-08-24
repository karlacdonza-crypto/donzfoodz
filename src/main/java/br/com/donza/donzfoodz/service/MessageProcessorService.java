package br.com.donza.donzfoodz.service;

import br.com.donza.donzfoodz.dto.Pedido;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class MessageProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessorService.class);
    private final GravaPedidoService gravaPedidoService;
    private final ObjectMapper objectMapper;

    public MessageProcessorService(GravaPedidoService gravaPedidoService, ObjectMapper objectMapper) {
        this.gravaPedidoService = gravaPedidoService;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule()); // Registra o módulo para suporte a LocalDateTime
    }

    public void processMessage(String mensagem) {
        try {
            // Desserializa o JSON para a classe Pedido
            Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
            logger.debug("Pedido recebido: {}", pedido.getNumero());
            // Chama o serviço para salvar o pedido
            gravaPedidoService.salvarPedido(pedido.getNumero());
        } catch (JsonProcessingException e) {
            logger.error("Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }
}
