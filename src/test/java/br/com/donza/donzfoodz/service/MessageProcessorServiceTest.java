package br.com.donza.donzfoodz.service;

import br.com.donza.donzfoodz.dto.Pedido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class MessageProcessorServiceTest {

    private final GravaPedidoService gravaPedidoService = mock(GravaPedidoService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageProcessorService messageProcessorService =
            new MessageProcessorService(gravaPedidoService, objectMapper);

    @Test
    void deveProcessarMensagemComSucesso() throws Exception {
        String json = "{\"numeroPedido\":\"12345\"}";
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido("12345");

        messageProcessorService.processMessage(json);

        verify(gravaPedidoService, times(1)).salvarPedido("12345");
    }

    @Test
    void deveLancarErroQuandoJsonInvalido() {
        String jsonInvalido = "invalido";

        messageProcessorService.processMessage(jsonInvalido);

        verifyNoInteractions(gravaPedidoService);
    }
}