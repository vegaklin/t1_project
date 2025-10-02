package ru.t1.accountprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.accountprocessing.dto.ClientTransactionDto;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.entity.Card;
import ru.t1.accountprocessing.entity.Payment;
import ru.t1.accountprocessing.entity.Transaction;
import ru.t1.accountprocessing.exception.CardNotFoundException;
import ru.t1.accountprocessing.model.ClientStatus;
import ru.t1.accountprocessing.model.TransactionStatus;
import ru.t1.accountprocessing.model.TransactionPaymentType;
import ru.t1.accountprocessing.repository.AccountRepository;
import ru.t1.accountprocessing.repository.CardRepository;
import ru.t1.accountprocessing.repository.PaymentRepository;
import ru.t1.accountprocessing.repository.TransactionRepository;
import ru.t1.accountprocessing.service.TransactionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Value("${t1.transaction.max-per-period}")
    private int maxTransactionsPerPeriod;

    @Value("${t1.transaction.period-duration-minutes}")
    private long transactionPeriodMinutes;

    @Override
    @Transactional
    public void createTransaction(ClientTransactionDto clientTransactionDto) {
        Card card = loadCard(clientTransactionDto.cardId());
        Account account = card.getAccount();

        if (isAccountInactive(account)) {
            return;
        }

        if (isLimitExceed(card.getCardId())) {
            log.warn("Account {} is blocked for limit exceed", account.getId());
            blockAccount(account);
            return;
        }

        processTransaction(clientTransactionDto, account, card);

        if (account.getIsRecalc()) {
            generatePaymentSchedule(account);
            if (clientTransactionDto.type() == TransactionPaymentType.DEPOSIT) {
                processCreditPayment(account, card);
            }
        }
    }

    private Card loadCard(String cardId) {
        return cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardId));
    }

    private boolean isAccountInactive(Account account) {
        if (account.getStatus() != ClientStatus.ACTIVE) {
            log.warn("Account {} is blocked, closed, or arrested, skip transaction", account.getId());
            return true;
        }
        return false;
    }

    private void processTransaction(ClientTransactionDto dto, Account account, Card card) {
        Transaction transaction = createTransaction(account, card, dto.type(), dto.amount(), TransactionStatus.PROCESSING);

        switch (dto.type()) {
            case DEPOSIT -> account.setBalance(account.getBalance().add(dto.amount()));
            case WITHDRAW -> {
                if (account.getBalance().compareTo(dto.amount()) < 0) {
                    log.warn("Account {} balance is less than transaction amount", account.getId());
                    transaction.setStatus(TransactionStatus.CANCELLED);
                    transactionRepository.save(transaction);
                    return;
                }
                account.setBalance(account.getBalance().subtract(dto.amount()));
            }
        }

        transaction.setStatus(TransactionStatus.COMPLETE);
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    private boolean isLimitExceed(String cardId) {
        LocalDateTime from = LocalDateTime.now().minusMinutes(transactionPeriodMinutes);
        long count = transactionRepository.countByCard_CardIdAndTimestampAfter(cardId, from).get();
        return count >= maxTransactionsPerPeriod;
    }

    private void blockAccount(Account account) {
        account.setStatus(ClientStatus.BLOCKED);
        if (account.getCards() != null) {
            account.getCards().forEach(card -> card.setStatus(ClientStatus.BLOCKED));
        }
        accountRepository.save(account);
    }

    private void generatePaymentSchedule(Account account) {
        if (paymentRepository.existsByAccount(account)) {
            return;
        }

        BigDecimal monthlyInterest = account.getBalance()
                .multiply(account.getInterestRate()
                        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP))
                .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);

        BigDecimal totalMonthlyPayment = account.getBalance().add(monthlyInterest);

        for (int i = 1; i <= 12; i++) {
            Payment payment = new Payment();
            payment.setAccount(account);
            payment.setAmount(totalMonthlyPayment);
            payment.setIsCredit(true);
            payment.setPaymentDate(LocalDate.now().plusMonths(i));
            payment.setType(TransactionPaymentType.WITHDRAW);
            payment.setExpired(false);

            paymentRepository.save(payment);
        }
    }

    private void processCreditPayment(Account account, Card card) {
        List<Payment> duePayments = paymentRepository.findByAccountAndPaymentDate(account, LocalDate.now());
        for (Payment payment : duePayments) {
            if (account.getBalance().compareTo(payment.getAmount()) >= 0) {
                if (payment.getPayedAt() == null) {
                    account.setBalance(account.getBalance().subtract(payment.getAmount()));
                    payment.setPayedAt(LocalDateTime.now());
                }
            } else {
                payment.setExpired(true);
            }
            paymentRepository.save(payment);
        }
        accountRepository.save(account);
    }

    private Transaction createTransaction(
            Account account,
            Card card,
            TransactionPaymentType type,
            BigDecimal amount,
            TransactionStatus transactionStatus
    ) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setStatus(transactionStatus);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
