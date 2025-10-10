package ru.t1.t1starter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.t1starter.aspect.CachedAspect;
import ru.t1.t1starter.service.CashedService;

@Configuration
public class CachedAspectConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1CachedAspect")
    public CachedAspect t1CachedAspect(
            @Qualifier("t1CashedService") CashedService t1CashedService
    ) {
        return new CachedAspect(t1CashedService);
    }
}
