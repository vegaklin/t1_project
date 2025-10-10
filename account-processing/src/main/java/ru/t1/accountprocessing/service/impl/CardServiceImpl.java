package ru.t1.accountprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.accountprocessing.dto.ClientCardDto;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.entity.Card;
import ru.t1.accountprocessing.model.ClientStatus;
import ru.t1.accountprocessing.repository.AccountRepository;
import ru.t1.accountprocessing.repository.CardRepository;
import ru.t1.accountprocessing.service.CardService;
import ru.t1.t1starter.annotation.LogDatasourceError;
import ru.t1.t1starter.annotation.Metric;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    @Override
    @Metric
    @LogDatasourceError
    @Transactional
    public void createCard(ClientCardDto clientCardDto) {
        Account account = accountRepository
                .findByClientIdAndProductId(clientCardDto.clientId(), clientCardDto.productId())
                .orElseThrow(() -> new RuntimeException( "Account not found for clientId: " + clientCardDto.clientId()
                        + " and productId: " + clientCardDto.productId()
                ));

        if (account.getStatus() == ClientStatus.ACTIVE) {
            Card card = new Card();
            card.setAccount(account);
            card.setCardId(UUID.randomUUID().toString());
            card.setPaymentSystem(clientCardDto.paymentSystem());
            card.setStatus(ClientStatus.ACTIVE);

            cardRepository.save(card);

            account.setCardExist(true);
            accountRepository.save(account);
        } else {
            log.info("Cannot create card for blocked account for clientId: {}", clientCardDto.clientId());
        }
    }
}