package ru.t1.t1starter.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "t1.cache")
public class CacheProperties {
    private long ttlSeconds;
}
