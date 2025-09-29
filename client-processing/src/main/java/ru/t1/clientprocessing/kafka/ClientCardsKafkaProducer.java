package ru.t1.clientprocessing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.clientprocessing.dto.ClientCardDto;

@Component
@RequiredArgsConstructor
public class ClientCardsKafkaProducer {

    @Value("${t1.kafka.topic.client-cards}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendClientCardDtoToKafka(ClientCardDto clientCardDto) {
        kafkaTemplate.send(topic, clientCardDto);
    }
}
