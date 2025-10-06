package ru.t1.clientprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.clientprocessing.aop.annotation.Metric;
import ru.t1.clientprocessing.dto.CardRequest;
import ru.t1.clientprocessing.dto.ClientCardDto;
import ru.t1.clientprocessing.kafka.ClientCardsKafkaProducer;
import ru.t1.clientprocessing.service.CardService;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final ClientCardsKafkaProducer clientCardsKafkaProducer;

    @Override
    @Metric
    public void createCard(CardRequest cardRequest) {
        ClientCardDto clientCardDto = new ClientCardDto(
                cardRequest.clientId(),
                cardRequest.productId(),
                cardRequest.paymentSystem()
        );
        clientCardsKafkaProducer.sendClientCardDtoToKafka(clientCardDto);
    }
}
