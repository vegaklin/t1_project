package ru.t1.t1starter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.t1starter.aspect.MetricAspect;
import ru.t1.t1starter.config.property.MetricProperties;
import ru.t1.t1starter.kafka.ServiceLogsProducer;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MetricProperties.class)
public class MetricAspectConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1MetricAspect")
    public MetricAspect t1MetricAspect(
            @Qualifier("t1ServiceLogsProducer") ServiceLogsProducer t1ServiceLogsProducer,
            @Qualifier("microserviceName") String microserviceName,
            MetricProperties metricProperties
    ) {
        return new MetricAspect(t1ServiceLogsProducer, metricProperties,microserviceName);
    }
}
