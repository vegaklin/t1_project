package ru.t1.t1starter.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.t1.t1starter.config.property.KafkaProperties;
import ru.t1.t1starter.model.LogType;

@Slf4j
@RequiredArgsConstructor
public class ServiceLogsProducer {

    private final KafkaProperties kafkaProperties;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendLogMessageToKafka(
            String microserviceName,
            String payload,
            LogType logType
    ) {
        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, kafkaProperties.getTopic().getServiceLogs())
                .setHeader(KafkaHeaders.KEY, microserviceName)
                .setHeader("type", logType.toString())
                .build();

        try {
            kafkaTemplate.send(message);
            log.info("Kafka send message: {}", message);
        } catch (Exception e) {
            log.error("Kafka sending exception: {}", message, e);
            throw e;
        }
    }
}
