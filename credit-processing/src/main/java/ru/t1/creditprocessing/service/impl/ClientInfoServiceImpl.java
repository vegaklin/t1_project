package ru.t1.creditprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.creditprocessing.client.ClientProcessingClient;
import ru.t1.creditprocessing.dto.ClientInfoResponse;
import ru.t1.creditprocessing.service.ClientInfoService;
import ru.t1.creditprocessing.util.JwtUtils;
import ru.t1.t1starter.annotation.Metric;

@Service
@RequiredArgsConstructor
public class ClientInfoServiceImpl implements ClientInfoService {

    private final ClientProcessingClient clientProcessingClient;

    private final JwtUtils jwtUtils;

    @Value("${spring.application.name}")
    private String microserviceName;

    @Override
    @Metric
    public ClientInfoResponse getClientInfo(String clientId) {
        String token = jwtUtils.generateJwtToken(microserviceName);
        
        return clientProcessingClient.gitClientInfo(clientId, token).block();
    }
}