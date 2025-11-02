package ru.t1.t1starter.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "t1.kafka")
public class KafkaProperties {

    private Bootstrap bootstrap = new Bootstrap();
    private Topic topic = new Topic();

    @Getter
    @Setter
    public static class Bootstrap {
        private String server;
    }

    @Getter
    @Setter
    public static class Topic {
        private String serviceLogs;
    }
}
