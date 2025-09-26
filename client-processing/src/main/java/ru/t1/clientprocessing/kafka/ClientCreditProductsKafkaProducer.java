package ru.t1.clientprocessing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.clientprocessing.dto.ClientCreditProductDto;

@Component
@RequiredArgsConstructor
public class ClientCreditProductsKafkaProducer {

    @Value("${t1.kafka.topic.client_credit_products}")
    private String topic;

    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendClientCardDtoToKafka(ClientCreditProductDto clientCreditProductDto) {
        kafkaTemplate.send(topic, clientCreditProductDto);
    }
}
