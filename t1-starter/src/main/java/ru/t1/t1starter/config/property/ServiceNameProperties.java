package ru.t1.t1starter.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceNameProperties {

    @Bean
    @ConditionalOnMissingBean(name = "microserviceName")
    public String microserviceName(@Value("${spring.application.name}") String name) {
        return name;
    }
}
