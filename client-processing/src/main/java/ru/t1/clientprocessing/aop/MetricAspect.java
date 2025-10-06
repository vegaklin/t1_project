package ru.t1.clientprocessing.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.clientprocessing.kafka.ServiceLogsProducer;
import ru.t1.clientprocessing.model.LogType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final ServiceLogsProducer serviceLogsProducer;

    @Value("${t1.metric.time-limit-ms}")
    private long timeLimitMs;

    @Value("${spring.application.name}")
    private String microserviceName;

    @Around("@annotation(ru.t1.clientprocessing.aop.annotation.Metric)")
    public Object metricTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            if (executionTime > timeLimitMs) {
                String message = createExecutionTimeMessage(joinPoint, executionTime);

                serviceLogsProducer.sendLogMessageToKafka(microserviceName, message, LogType.WARNING);
                log.warn("Method {} exceeded time ({} ms > {} ms)", joinPoint.getSignature().toShortString(), executionTime, timeLimitMs);
            }
        }
    }

    private String createExecutionTimeMessage(JoinPoint joinPoint, long executionTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("method", joinPoint.getSignature().toShortString());
        map.put("args", Arrays.toString(joinPoint.getArgs()));
        map.put("executionTimeMs", executionTime);
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
