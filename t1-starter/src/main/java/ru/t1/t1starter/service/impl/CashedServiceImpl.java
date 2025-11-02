package ru.t1.t1starter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import ru.t1.t1starter.config.property.CacheProperties;
import ru.t1.t1starter.service.CashedService;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CashedServiceImpl implements CashedService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final CacheProperties cacheProperties;

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
        redisTemplate.opsForValue().set(key, value, cacheProperties.getTtlSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return objectMapper.convertValue(value, clazz);
    }
}
