package ru.t1.t1starter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.t1starter.aspect.HttpRequestLogAspect;
import ru.t1.t1starter.kafka.ServiceLogsProducer;

@Configuration
public class HttpRequestLogAspectConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1HttpRequestLogAspect")
    public HttpRequestLogAspect t1HttpRequestLogAspect(
            @Qualifier("t1ServiceLogsProducer") ServiceLogsProducer t1ServiceLogsProducer,
            @Qualifier("microserviceName") String microserviceName
    ) {
        return new HttpRequestLogAspect(t1ServiceLogsProducer, microserviceName);
    }
}
