package ru.t1.clientprocessing;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainersConfiguration {

//    @Bean
//    @RestartScope
//    @ServiceConnection
//    public PostgreSQLContainer<?> postgreSQLContainer() {
//        return new PostgreSQLContainer<>("postgres:16")
//                .withDatabaseName("testdb")
//                .withUsername("test")
//                .withPassword("test");
//    }

//    static {
//        POSTGRE_SQL_CONTAINER.start();
//    }

//    @DynamicPropertySource
//    static void registerProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
//        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
//        registry.add("spring.liquibase.enabled", () -> true);
//        registry.add("spring.liquibase.change-log", () -> "classpath:db/changelog/master.xml");

//        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
//
//        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
//        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);
//    }
}
