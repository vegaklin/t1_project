package ru.t1.t1starter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.t1starter.config.property.KafkaProperties;
import ru.t1.t1starter.kafka.ServiceLogsProducer;
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class ServiceLogsProducerConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1ServiceLogsProducer")
    public ServiceLogsProducer t1ServiceLogsProducer(
            @Qualifier("t1KafkaTemplate") KafkaTemplate<String, Object> t1KafkaTemplate,
            KafkaProperties kafkaProperties
    ) {
        return new ServiceLogsProducer(kafkaProperties, t1KafkaTemplate);
    }
}
