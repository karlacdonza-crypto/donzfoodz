package br.com.donza.donzfoodz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsMessageSender {

    private final SqsClient sqsClient;

    @Value("${sqs.target-queue-url}")
    private String targetQueueUrl;

    public SqsMessageSender(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendMessage(String message) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(targetQueueUrl)
                .messageBody(message)
                .build();
        sqsClient.sendMessage(sendMessageRequest);
    }
}