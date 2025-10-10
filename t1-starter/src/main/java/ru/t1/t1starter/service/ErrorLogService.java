package ru.t1.t1starter.service;

import ru.t1.t1starter.model.LogType;

public interface ErrorLogService {
    void saveError(String microserviceName, String message, LogType logType);
}
