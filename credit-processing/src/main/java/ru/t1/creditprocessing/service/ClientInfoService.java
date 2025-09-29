package ru.t1.creditprocessing.service;

import ru.t1.creditprocessing.dto.ClientInfoResponse;

public interface ClientInfoService {
    ClientInfoResponse getClientInfo(String clientId);
}
