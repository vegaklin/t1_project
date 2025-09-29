package ru.t1.accountprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.t1.accountprocessing.dto.ClientTransactionDto;
import ru.t1.accountprocessing.service.TransactionService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientTransactionKafkaConsumer {

    private final TransactionService transactionService;

    @KafkaListener(
            topics = "${t1.kafka.topic.client-transactions}",
            containerFactory = "clientTransactionKafkaListenerContainerFactory")
    public void listen(
            List<ClientTransactionDto> messageList,
            Acknowledgment ack
    ) {
        try {
            messageList.forEach(dto -> {
                log.info("Processing ClientTransactionDto: {}", dto);
                transactionService.createTransaction(dto);
            });
        } catch (Exception e) {
            log.error("Error processing ClientTransactionDto: {}", e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }
}
