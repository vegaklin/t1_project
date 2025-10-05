package ru.t1.creditprocessing.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.t1.creditprocessing.kafka.ServiceLogsProducer;
import ru.t1.creditprocessing.model.LogType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpRequestLogAspect {

    private final ServiceLogsProducer serviceLogsProducer;

    @Value("${spring.application.name}")
    private String microserviceName;

    @AfterReturning(
            pointcut = "@annotation(ru.t1.creditprocessing.aop.annotation.HttpOutcomeRequestLog)",
            returning = "result"
    )
    public void logHttpOutcome(JoinPoint joinPoint, Object result) {
        String message = createHttpOutcomeMessage(joinPoint, result);
        sendToKafka(message);
        log.info("HTTP outcome: {}", message);
    }

    private String createHttpOutcomeMessage(JoinPoint joinPoint, Object result) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("method", joinPoint.getSignature().toShortString());
        map.put("args", Arrays.toString(joinPoint.getArgs()));
        map.put("result", result);
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private void sendToKafka(String message) {
        serviceLogsProducer.sendLogMessageToKafka(microserviceName, message, LogType.INFO);
    }
}
