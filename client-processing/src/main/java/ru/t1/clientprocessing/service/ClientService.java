package ru.t1.clientprocessing.service;

import ru.t1.clientprocessing.dto.ClientInfoResponse;
import ru.t1.clientprocessing.dto.RegisterClientRequest;
import ru.t1.clientprocessing.dto.RegisterClientResponse;

public interface ClientService {
    RegisterClientResponse registerClient(RegisterClientRequest registerClientRequest);
    ClientInfoResponse getClientInfo(String clientId);
}
