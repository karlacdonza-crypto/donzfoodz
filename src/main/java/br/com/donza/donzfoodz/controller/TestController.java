package br.com.donza.donzfoodz.controller;

import br.com.donza.donzfoodz.service.SqsMessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

     @GetMapping("/test")
     public ResponseEntity<String> testEndpoint() {

         return ResponseEntity.ok("Teste bem-sucedido!");
             }

    private final SqsMessageSender sqsService;
    @Value("${sqs.queue-url}")
    private String queueUrl;

    public TestController(SqsMessageSender sqsService) {
        this.sqsService = sqsService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        sqsService.sendMessage(message);
        return "Mensagem enviada: " + message;
    }
}
