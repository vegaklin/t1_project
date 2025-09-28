package ru.t1.accountprocessing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.accountprocessing.dto.ClientCardDto;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.entity.Card;
import ru.t1.accountprocessing.model.Status;
import ru.t1.accountprocessing.repository.AccountRepository;
import ru.t1.accountprocessing.repository.CardRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void createCard(ClientCardDto clientCardDto) {
        Account account = accountRepository.findByClientId(clientCardDto.clientId())
                .orElseThrow(() -> new RuntimeException("Account not found for clientId: " + clientCardDto.clientId()));

        if (account.getStatus() != Status.BLOCKED) {
            Card card = new Card();
            card.setAccount(account);
            card.setCardId(UUID.randomUUID().toString());
            card.setPaymentSystem(clientCardDto.paymentSystem());
            card.setStatus(Status.ALLOWED);

            cardRepository.save(card);

            account.setCardExist(true);
            accountRepository.save(account);
        } else {
            log.info("Cannot create card for blocked account for clientId: {}", clientCardDto.clientId());
        }
    }
}
