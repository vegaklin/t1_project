package ru.t1.accountprocessing.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.accountprocessing.dto.ClientCardDto;
import ru.t1.accountprocessing.dto.ClientProductDto;
import ru.t1.accountprocessing.dto.ClientTransactionDto;
import ru.t1.accountprocessing.dto.ClientPaymentDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${t1.kafka.consumer.group-id}")
    private String groupId;
    @Value("${t1.kafka.bootstrap.server}")
    private String server;
    @Value("${t1.kafka.consumer.session.timeout.ms}")
    private String sessionTimeout;
    @Value("${t1.kafka.consumer.max.partition.fetch.bytes}")
    private String maxPartitionFetchBytes;
    @Value("${t1.kafka.consumer.max.poll.records}")
    private String maxPollRecords;
    @Value("${t1.kafka.consumer.max.poll.interval.ms}")
    private String maxPollIntervalsMs;
    @Value("${t1.kafka.consumer.heartbeat.interval}")
    private String heartbeatInterval;

    @Bean
    public ConsumerFactory<String, ClientProductDto> clientProductConsumerFactory() {
        Map<String, Object> props = consumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClientProductDto.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, ClientCardDto> clientCardConsumerFactory() {
        Map<String, Object> props = consumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClientCardDto.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, ClientTransactionDto> clientTransactionFactory() {
        Map<String, Object> props = consumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClientTransactionDto.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, ClientPaymentDto> clientPaymentFactory() {
        Map<String, Object> props = consumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClientPaymentDto.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, UUIDDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientProductDto> clientProductKafkaListenerContainerFactory() {
        return createListenerFactory(clientProductConsumerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientCardDto> clientCardKafkaListenerContainerFactory() {
        return createListenerFactory(clientCardConsumerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientTransactionDto> clientTransactionKafkaListenerContainerFactory() {
        return createListenerFactory(clientTransactionFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientPaymentDto> clientPaymentKafkaListenerContainerFactory() {
        return createListenerFactory(clientPaymentFactory());
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createListenerFactory(ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.error(" RetryListeners message = {}, offset = {} deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt);
        });
        return handler;
    }
}
