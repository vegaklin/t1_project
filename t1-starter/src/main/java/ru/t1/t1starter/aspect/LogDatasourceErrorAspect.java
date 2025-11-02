package ru.t1.t1starter.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import ru.t1.t1starter.kafka.ServiceLogsProducer;
import ru.t1.t1starter.model.LogType;
import ru.t1.t1starter.service.ErrorLogService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogDatasourceErrorAspect {

    private final ServiceLogsProducer serviceLogsProducer;

    private final ErrorLogService errorLogService;

    private final String microserviceName;

    @AfterThrowing(
            pointcut = "@annotation(ru.t1.t1starter.annotation.LogDatasourceError)",
            throwing = "throwable"
    )
    public void logError(JoinPoint joinPoint, Throwable throwable) {
        String message = createMessage(joinPoint, throwable);
        sendToKafka(message);
        log.error("Exception in method {}: {}", joinPoint.getSignature(), throwable.getMessage());
    }

    private String createMessage(JoinPoint joinPoint, Throwable throwable) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("method", joinPoint.getSignature().toShortString());
        map.put("exception", throwable.getClass().getName());
        map.put("message", throwable.getMessage());
        map.put("stackTrace", Arrays.toString(throwable.getStackTrace()));
        map.put("args", Arrays.toString(joinPoint.getArgs()));
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private void sendToKafka(String message) {
        try {
            serviceLogsProducer.sendLogMessageToKafka(microserviceName, message, LogType.ERROR);
        } catch (Exception e) {
            errorLogService.saveError(microserviceName, message, LogType.ERROR);
        }
    }
}
