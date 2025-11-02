package ru.t1.accountprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.t1.accountprocessing.dto.ClientProductDto;
import ru.t1.accountprocessing.service.AccountService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientProductKafkaConsumer {

    private final AccountService accountService;

    @KafkaListener(
            topics = "${t1.kafka.topic.client-products}",
            containerFactory = "clientProductKafkaListenerContainerFactory")
    public void listen(
            List<ClientProductDto> messageList,
            Acknowledgment ack
    ) {
        try {
            messageList.forEach(dto -> {
                log.info("Processing ClientProductDto: {}", dto);
                accountService.createAccount(dto);
            });
        } catch (Exception e) {
            log.error("Error processing ClientProductDto: {}", e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }
}
