package br.com.donza.donzfoodz.service;

import br.com.donza.donzfoodz.dto.Pedido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessorService.class);
    private final GravaPedidoService gravaPedidoService;
    private final ObjectMapper objectMapper;

    public MessageProcessorService(GravaPedidoService gravaPedidoService, ObjectMapper objectMapper) {
        this.gravaPedidoService = gravaPedidoService;
        this.objectMapper = objectMapper;
    }

    public void processMessage(String message) {
        try {
            // Desserializa o JSON para o objeto Pedido
            Pedido pedido = objectMapper.readValue(message, Pedido.class);

            // Salva apenas o número do pedido no banco
            gravaPedidoService.salvarPedido(pedido.getNumeroPedido());
            logger.info("Pedido processado com sucesso: {}", pedido.getNumeroPedido());;
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }
}