package ru.t1.accountprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.t1.accountprocessing.dto.ClientCardDto;
import ru.t1.accountprocessing.service.CardService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientCardKafkaConsumer {

    private final CardService cardService;

    @KafkaListener(
            topics = "${t1.kafka.topic.client-cards}",
            containerFactory = "clientCardKafkaListenerContainerFactory")
    public void listen(
            List<ClientCardDto> messageList,
            Acknowledgment ack
    ) {
        try {
            messageList.forEach(dto -> {
                log.info("Processing ClientCardDto: {}", dto);
                cardService.createCard(dto);
            });
        } catch (RuntimeException e) {
            log.error("Error processing ClientCardDto: {}", e.getMessage(), e);
            throw e;
        } finally {
            ack.acknowledge();
        }
    }
}
