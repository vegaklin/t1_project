package ru.t1.clientprocessing.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.t1.clientprocessing.service.CashedService;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CashedServiceImpl implements CashedService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Value("${t1.cache.ttl-seconds}")
    private long ttlSeconds;

    @Override
    public String generateKey(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        if (args == null || args.length == 0) {
            return className + ":" + methodName;
        }

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(className).append(":").append(methodName).append(":");
        for (Object arg : args) {
            if (arg == null) {
                keyBuilder.append("null");
            } else if (BeanUtils.isSimpleValueType(arg.getClass())) {
                keyBuilder.append(arg);
            } else if (arg.getClass().isArray()) {
                keyBuilder.append(Arrays.deepHashCode(new Object[]{arg}));
            } else {
                keyBuilder.append(arg.hashCode());
            }
            keyBuilder.append("-");
        }

        return keyBuilder.toString();
    }

    @Override
    public void saveToRedis(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return objectMapper.convertValue(value, clazz);
    }
}
