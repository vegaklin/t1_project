package ru.t1.clientprocessing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.clientprocessing.dto.ClientProductDto;

@Component
@RequiredArgsConstructor
public class ClientProductsKafkaProducer {

    @Value("${t1.kafka.topic.client-products}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendClientProductDtoToKafka(ClientProductDto clientProductDto) {
        kafkaTemplate.send(topic, clientProductDto);
    }
}
