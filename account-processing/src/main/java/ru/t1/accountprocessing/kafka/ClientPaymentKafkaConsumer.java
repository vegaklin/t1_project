package ru.t1.accountprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.t1.accountprocessing.dto.ClientPaymentDto;
import ru.t1.accountprocessing.service.PaymentService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientPaymentKafkaConsumer {

    private final PaymentService paymentService;

    @KafkaListener(
            topics = "${t1.kafka.topic.client-payments}",
            containerFactory = "clientPaymentKafkaListenerContainerFactory")
    public void listen(
            List<ConsumerRecord<UUID, ClientPaymentDto>> messageList,
            Acknowledgment ack
    ) {
        try {
            for (var message : messageList) {
                log.info("Processing transaction key={} ClientPaymentDto={}", message.key(), message.value());
                paymentService.createPayment(message.value());
            }
        } catch (Exception e) {
            log.error("Error processing ClientTransactionDto: {}", e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }
}