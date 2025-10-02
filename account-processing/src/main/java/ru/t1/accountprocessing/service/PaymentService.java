package ru.t1.accountprocessing.service;

import ru.t1.accountprocessing.dto.ClientPaymentDto;

public interface PaymentService {
    void createPayment(ClientPaymentDto clientPaymentDto);
}
