package br.com.donza.donzfoodz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@Service
public class SqsMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(SqsMessageListener.class);
    private final MessageProcessorService messageProcessorService;

    public SqsMessageListener(MessageProcessorService messageProcessorService) {
        this.messageProcessorService = messageProcessorService;
    }

    @SqsListener("${sqs.queue-url}")
    public void listenToQueue(String message) {
        try {
            logger.info("Mensagem recebida: {}", message);
            messageProcessorService.processMessage(message);
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }
}