package ru.t1.creditprocessing.service;

import ru.t1.creditprocessing.model.LogType;

public interface ErrorLogService {
    void saveError(String microserviceName, String message, LogType logType);
}
