package ru.t1.accountprocessing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.accountprocessing.dto.ClientProductDto;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.model.ClientStatus;
import ru.t1.accountprocessing.model.Status;
import ru.t1.accountprocessing.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void createAccount(ClientProductDto clientProductDto) {
        Account account = new Account();

        account.setClientId(clientProductDto.clientId());
        account.setProductId(clientProductDto.productId());
        account.setBalance(BigDecimal.ZERO);
        account.setInterestRate(BigDecimal.ZERO);
        account.setIsRecalc(false);
        account.setCardExist(false);
        if (clientProductDto.status() == ClientStatus.ACTIVE) {
            account.setStatus(Status.ALLOWED);
        }
        else {
            account.setStatus(Status.CANCELLED);
        }

        accountRepository.save(account);
    }
}
