package ru.t1.clientprocessing.service;

import org.aspectj.lang.ProceedingJoinPoint;

public interface CashedService {
    String generateKey(ProceedingJoinPoint joinPoint);
    void saveToRedis(String key, Object value);
    public <T> T getValue(String key, Class<T> clazz);
}
