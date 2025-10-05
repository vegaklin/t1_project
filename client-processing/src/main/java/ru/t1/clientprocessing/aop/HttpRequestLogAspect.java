package ru.t1.clientprocessing.aop;

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
public class HttpRequestLogAspect {

    private final ServiceLogsProducer serviceLogsProducer;

    @Value("${spring.application.name}")
    private String microserviceName;

    @Before("@annotation(ru.t1.clientprocessing.aop.annotation.HttpIncomeRequestLog)")
    public void logHttpIncome(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String message = createHttpIncomeMessage(joinPoint, request);
        sendToKafka(message);
        log.info("HTTP income: {}", message);
    }

    private String createHttpIncomeMessage(JoinPoint joinPoint, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("method", joinPoint.getSignature().toShortString());
        map.put("URI", request.getRequestURI());
        map.put("params", request.getParameterMap());
        map.put("args", Arrays.toString(joinPoint.getArgs()));
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
