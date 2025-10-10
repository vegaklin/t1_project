package ru.t1.t1starter.service;

import org.aspectj.lang.ProceedingJoinPoint;

public interface CashedService {
    String generateKey(ProceedingJoinPoint joinPoint);
    void saveToRedis(String key, Object value);
    <T> T getValue(String key, Class<T> clazz);
}
