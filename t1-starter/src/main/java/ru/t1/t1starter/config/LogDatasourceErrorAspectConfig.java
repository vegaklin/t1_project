package ru.t1.t1starter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.t1starter.aspect.LogDatasourceErrorAspect;
import ru.t1.t1starter.kafka.ServiceLogsProducer;
import ru.t1.t1starter.service.ErrorLogService;

@Configuration
public class LogDatasourceErrorAspectConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1LogDatasourceErrorAspect")
    public LogDatasourceErrorAspect t1LogDatasourceErrorAspect(
            @Qualifier("t1ServiceLogsProducer") ServiceLogsProducer t1ServiceLogsProducer,
            @Qualifier("t1ErrorLogService") ErrorLogService t1ErrorLogService,
            @Qualifier("microserviceName") String microserviceName
    ) {
        return new LogDatasourceErrorAspect(t1ServiceLogsProducer, t1ErrorLogService, microserviceName);
    }
}
