package br.com.donza.donzfoodz.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

class SqsMessageListenerTest {

    private final MessageProcessorService messageProcessorService = mock(MessageProcessorService.class);
    private final SqsMessageListener sqsMessageListener = new SqsMessageListener(messageProcessorService);

    @Test
    void deveChamarProcessMessageComMensagemValida() throws Exception {
        // Lê a mensagem do arquivo JSON
        String mensagem = Files.readString(Path.of("src/test/resources/mensagem-teste.json"));

        sqsMessageListener.listenToQueue(mensagem);

        verify(messageProcessorService, times(1)).processMessage(mensagem);
    }

    @Test
    void deveLancarErroQuandoProcessamentoFalhar() throws Exception {
        // Lê a mensagem do arquivo JSON
        String mensagem = Files.readString(Path.of("src/test/resources/mensagem-teste.json"));
        doThrow(new RuntimeException("Erro")).when(messageProcessorService).processMessage(mensagem);

        sqsMessageListener.listenToQueue(mensagem);

        verify(messageProcessorService, times(1)).processMessage(mensagem);
    }
}