package ru.t1.t1starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import ru.t1.t1starter.config.property.CacheProperties;
import ru.t1.t1starter.service.CashedService;
import ru.t1.t1starter.service.impl.CashedServiceImpl;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CashedServiceConfig {

    @Bean
    @ConditionalOnMissingBean(name = "t1CashedService")
    public CashedService t1CashedService(
            @Qualifier("t1RedisTemplate") RedisTemplate<String, Object> t1RedisTemplate,
            @Qualifier("t1RedisObjectMapper") ObjectMapper t1RedisObjectMapper,
            CacheProperties cacheProperties
    ) {
        return new CashedServiceImpl(t1RedisTemplate, t1RedisObjectMapper, cacheProperties);
    }
}
