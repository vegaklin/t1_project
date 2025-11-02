package ru.t1.accountprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.t1.accountprocessing.dto.ClientTransactionDto;
import ru.t1.accountprocessing.service.TransactionService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientTransactionKafkaConsumer {

    private final TransactionService transactionService;

    @KafkaListener(
            topics = "${t1.kafka.topic.client-transactions}",
            containerFactory = "clientTransactionKafkaListenerContainerFactory")
    public void listen(
            List<ConsumerRecord<UUID, ClientTransactionDto>> messageList,
            Acknowledgment ack
    ) {
        try {
            for (var message : messageList) {
                log.info("Processing transaction key={} ClientTransactionDto={}", message.key(), message.value());
                transactionService.createTransaction(message.value());
            }
        } catch (Exception e) {
            log.error("Error processing ClientTransactionDto: {}", e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }
}
