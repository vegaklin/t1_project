package ru.t1.accountprocessing.service;

import ru.t1.accountprocessing.dto.ClientTransactionDto;

public interface TransactionService {
    void createTransaction(ClientTransactionDto clientProductDto);
}
