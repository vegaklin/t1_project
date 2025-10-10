package ru.t1.accountprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.accountprocessing.dto.ClientProductDto;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.repository.AccountRepository;
import ru.t1.accountprocessing.service.AccountService;
import ru.t1.t1starter.annotation.Metric;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Value("${t1.account.interest-rate-percent}")
    private BigDecimal interestRate;

    @Override
    @Metric
    @Transactional
    public void createAccount(ClientProductDto clientProductDto) {
        Account account = new Account();

        account.setClientId(clientProductDto.clientId());
        account.setProductId(clientProductDto.productId());
        account.setBalance(BigDecimal.ZERO);
        account.setCardExist(false);
        account.setStatus(clientProductDto.status());

        if (clientProductDto.isRecalc()) {
            account.setInterestRate(interestRate);
            account.setIsRecalc(true);
        }
        else {
            account.setInterestRate(BigDecimal.ZERO);
            account.setIsRecalc(false);
        }

        accountRepository.save(account);
    }
}
