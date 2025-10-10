package ru.t1.t1starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.t1.t1starter.repository.ErrorLogRepository;
import ru.t1.t1starter.service.ErrorLogService;
import ru.t1.t1starter.service.impl.ErrorLogServiceImpl;

@Configuration
@EnableJpaRepositories(basePackages = "ru.t1.t1starter.repository")
@EntityScan(basePackages = "ru.t1.t1starter.entity")
public class ErrorLogServiceConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1ErrorLogService")
    public ErrorLogService t1ErrorLogService(
            ErrorLogRepository errorLogRepository
    ) {
        return new ErrorLogServiceImpl(errorLogRepository);
    }
}
