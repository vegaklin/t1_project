package ru.t1.creditprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.creditprocessing.client.ClientProcessingClient;
import ru.t1.creditprocessing.dto.ClientInfoResponse;
import ru.t1.creditprocessing.service.ClientInfoService;

@Service
@RequiredArgsConstructor
public class ClientInfoServiceImpl implements ClientInfoService {

    private final ClientProcessingClient clientProcessingClient;

    @Override
    public ClientInfoResponse getClientInfo(String clientId) {
        return clientProcessingClient.gitClientInfo(clientId).block();
    }
}