package ru.t1.clientprocessing.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.t1.clientprocessing.service.CashedService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CachedAspect {

    private final CashedService cashedService;

    @Around("@annotation(ru.t1.clientprocessing.aop.annotation.Cached)")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = cashedService.generateKey(joinPoint);
        Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        Object value = cashedService.getValue(key, returnType);
        if (value != null) {
            log.info("Get value from cache: {}", key);
            return value;
        }

        Object result = joinPoint.proceed();
        if (result != null) {
            log.info("Put value to cache: {}", key);
            cashedService.saveToRedis(key, result);
        }
        return result;
    }
}
