package ru.t1.clientprocessing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.clientprocessing.dto.CardRequest;
import ru.t1.clientprocessing.dto.ClientCardDto;
import ru.t1.clientprocessing.kafka.ClientCardsKafkaProducer;

@Service
@RequiredArgsConstructor
public class CardService {

    private final ClientCardsKafkaProducer clientCardsKafkaProducer;

    public void createCard(CardRequest cardRequest) {
        ClientCardDto clientCardDto = new ClientCardDto(
                cardRequest.clientId(),
                cardRequest.paymentSystem()
        );
        clientCardsKafkaProducer.sendClientCardDtoToKafka(clientCardDto);
    }
}
