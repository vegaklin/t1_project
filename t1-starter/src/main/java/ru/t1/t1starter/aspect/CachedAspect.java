package ru.t1.t1starter.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.t1.t1starter.service.CashedService;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class CachedAspect {

    private final CashedService cashedService;

    @Around("@annotation(ru.t1.t1starter.annotation.Cached)")
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
