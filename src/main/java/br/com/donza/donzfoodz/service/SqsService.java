package br.com.donza.donzfoodz.service;
import br.com.donza.donzfoodz.entity.GravaPedido;
import br.com.donza.donzfoodz.repository.GravaPedidoRepository;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

    private final GravaPedidoRepository gravaPedidoRepository;
    private final SqsClient sqsClient;

    @Value("${sqs.target-queue-url}")
    private String targetQueueUrl;

    public SqsService(GravaPedidoRepository gravaPedidoRepository, SqsClient sqsClient) {
        this.gravaPedidoRepository = gravaPedidoRepository;
        this.sqsClient = sqsClient;
    }

    @SqsListener("${sqs.queue-url}")
    public void listenToQueue(String message) {
        try {
            // Processar a mensagem recebida
            System.out.println("Mensagem recebida: " + message);
            processMessage(message);
            sendMessage(targetQueueUrl, message);
        } catch (Exception e) {
            // Log de erro (opcional)
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }

    private void processMessage(String message) {
        // Criar uma nova entidade GravaPedido
        GravaPedido gravaPedido = new GravaPedido();
        gravaPedido.setMessage(message);

        // Salvar a entidade no banco de dados
        gravaPedidoRepository.save(gravaPedido);
    }

    public void sendMessage(String queueUrl, String message) {
        // Criar a requisição para enviar a mensagem
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();

        // Enviar a mensagem para a fila
        sqsClient.sendMessage(sendMessageRequest);
    }
}