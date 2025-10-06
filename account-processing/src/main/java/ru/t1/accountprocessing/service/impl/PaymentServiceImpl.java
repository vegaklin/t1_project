package ru.t1.accountprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.accountprocessing.dto.ClientPaymentDto;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.entity.Payment;
import ru.t1.accountprocessing.exception.AccountNotFoundException;
import ru.t1.accountprocessing.repository.AccountRepository;
import ru.t1.accountprocessing.repository.PaymentRepository;
import ru.t1.accountprocessing.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void createPayment(ClientPaymentDto clientPaymentDto) {
        Account account = getCreditAccount(clientPaymentDto);
        if (!account.getIsRecalc()) {
            log.info("Account {} is not a credit account", account.getId());
            return;
        }

        BigDecimal totalDebt = calculateDebt(account);
        if (!isPaymentValid(clientPaymentDto.amount(), totalDebt)) {
            return;
        }

        LocalDateTime payedAt = LocalDateTime.now();
        markPaymentsAsPaid(account, payedAt);

        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setAmount(clientPaymentDto.amount());
        payment.setType(clientPaymentDto.type());
        payment.setIsCredit(true);
        payment.setPaymentDate(LocalDate.now());
        payment.setPayedAt(payedAt);
        payment.setExpired(false);
        paymentRepository.save(payment);
    }

    private Account getCreditAccount(ClientPaymentDto clientPaymentDto) {
        return accountRepository.findByClientIdAndProductId(clientPaymentDto.clientId(), clientPaymentDto.productId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + clientPaymentDto));
    }

    private BigDecimal calculateDebt(Account account) {
        return paymentRepository.findByAccountAndIsCreditTrue(account).stream()
                .filter(p -> p.getPayedAt() == null)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isPaymentValid(BigDecimal amount, BigDecimal totalDebt) {
        if (amount.compareTo(totalDebt) != 0) {
            log.warn("Payment amount {} not equal total debt {}", amount, totalDebt);
            return false;
        }
        return true;
    }

    private void markPaymentsAsPaid(Account account, LocalDateTime payedAt) {
        List<Payment> payments = paymentRepository.findByAccount(account);
        for (Payment payment : payments) {
            if (payment.getIsCredit() && payment.getPayedAt() == null) {
                payment.setPayedAt(payedAt);
            }
        }
        if (!payments.isEmpty()) {
            paymentRepository.saveAll(payments);
        }
    }
}