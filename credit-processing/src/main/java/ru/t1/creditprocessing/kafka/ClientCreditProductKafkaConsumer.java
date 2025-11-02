package ru.t1.creditprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.t1.creditprocessing.dto.ClientCreditProductDto;
import ru.t1.creditprocessing.service.ClientCreditService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientCreditProductKafkaConsumer {

    private final ClientCreditService clientCreditService;

    @KafkaListener(
            topics = "${t1.kafka.topic.client-credit-products}",
            containerFactory = "clientCreditProductKafkaListenerContainerFactory")
    public void listen(
            List<ClientCreditProductDto> messageList,
            Acknowledgment ack
    ) {
        try {
            messageList.forEach(dto -> {
                log.info("Processing ClientCreditProductDto: {}", dto);
                clientCreditService.createCredit(dto);
            });
        } catch (Exception e) {
            log.error("Error processing ClientCreditProductDto: {}", e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }
}
