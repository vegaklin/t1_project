package ru.t1.creditprocessing.service;

import ru.t1.creditprocessing.dto.ClientCreditProductDto;

public interface ClientCreditService {
    void createCredit(ClientCreditProductDto clientCreditProductDto);
}
