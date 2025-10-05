package ru.t1.creditprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.creditprocessing.model.LogType;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceLogsProducer {

    @Value("${t1.kafka.topic.service-logs}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendLogMessageToKafka(
            String microserviceName,
            String payload,
            LogType logType
    ) {
        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, microserviceName)
                .setHeader("type", logType.toString())
                .build();

        try {
            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.error("Kafka sending exception: {}", message, e);
            throw e;
        }
    }
}
