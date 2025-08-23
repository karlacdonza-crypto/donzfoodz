package br.com.donza.donzfoodz.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
@Service
public class SqsService {

    // Aqui você pode implementar métodos para enviar e receber mensagens do SQS
    // Por exemplo, um método para enviar uma mensagem para uma fila específica

     private final SqsClient sqsClient;

     public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
     }

     public void sendMessage(String queueUrl, String messageBody) {
         SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                 .queueUrl(queueUrl)
                 .messageBody(messageBody)
                 .build();
         sqsClient.sendMessage(sendMsgRequest);
     }
}
