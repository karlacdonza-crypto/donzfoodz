package br.com.donza.donzfoodz.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class SqsMessageListenerTest {

    private final MessageProcessorService messageProcessorService = mock(MessageProcessorService.class);
    private final SqsMessageListener sqsMessageListener = new SqsMessageListener(messageProcessorService);

    @Test
    void deveChamarProcessMessageComMensagemValida() {
        String mensagem = "{\"numeroPedido\":\"12345\"}";

        sqsMessageListener.listenToQueue(mensagem);

        verify(messageProcessorService, times(1)).processMessage(mensagem);
    }

    @Test
    void deveLancarErroQuandoProcessamentoFalhar() {
        String mensagem = "{\"numeroPedido\":\"12345\"}";
        doThrow(new RuntimeException("Erro")).when(messageProcessorService).processMessage(mensagem);

        sqsMessageListener.listenToQueue(mensagem);

        verify(messageProcessorService, times(1)).processMessage(mensagem);
    }
}