package ru.t1.creditprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.creditprocessing.entity.ErrorLog;
import ru.t1.creditprocessing.model.LogType;
import ru.t1.creditprocessing.repository.ErrorLogRepository;
import ru.t1.creditprocessing.service.ErrorLogService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogRepository errorLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveError(
            String microserviceName,
            String message,
            LogType logType
    ) {
        ErrorLog logEntity = new ErrorLog();
        logEntity.setMicroserviceName(microserviceName);
        logEntity.setType(logType);
        logEntity.setMessage(message);
        logEntity.setCreatedAt(LocalDateTime.now());
        errorLogRepository.save(logEntity);
    }
}
