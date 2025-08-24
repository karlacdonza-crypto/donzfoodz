package br.com.donza.donzfoodz.service;

import br.com.donza.donzfoodz.dto.Pedido;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

class MessageProcessorServiceTest {

    private final GravaPedidoService gravaPedidoService = mock(GravaPedidoService.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(DeserializationFeature.UNWRAP_ROOT_VALUE); // Configuração para propriedades encapsuladas
    private final MessageProcessorService messageProcessorService =
            new MessageProcessorService(gravaPedidoService, objectMapper);

    @Test
    void deveProcessarMensagemComSucesso() throws Exception {
        // Lê a mensagem do arquivo JSON
        String mensagem = Files.readString(Path.of("src/test/resources/mensagem-teste.json"));

        // Configura o mock para não lançar exceções
        doNothing().when(gravaPedidoService).salvarPedido(anyString());

        // Executa o método a ser testado
        messageProcessorService.processMessage(mensagem);

        // Verifica se o método foi chamado com o valor correto
        verify(gravaPedidoService, times(1)).salvarPedido("e4a1b0c6-1f37-5d0f-9f1e-2b9f0e4c8d6a");
    }

    @Test
    void deveLancarErroQuandoJsonInvalido() {
        String jsonInvalido = "invalido";

        // Executa o método com JSON inválido
        messageProcessorService.processMessage(jsonInvalido);

        // Verifica que não houve interações com o mock
        verifyNoInteractions(gravaPedidoService);
    }
}