package ru.t1.clientprocessing.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.clientprocessing.dto.CardRequest;
import ru.t1.clientprocessing.dto.ClientCardDto;
import ru.t1.clientprocessing.kafka.ClientCardsKafkaProducer;
import ru.t1.clientprocessing.model.PaymentSystem;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private ClientCardsKafkaProducer kafkaProducer;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void checkCreateCard_sendClientCardDtoToKafka_correctData() {
        // given
        CardRequest cardRequest = new CardRequest("123", "456", PaymentSystem.VISA);

        ClientCardDto clientCardDto = new ClientCardDto(
                cardRequest.clientId(),
                cardRequest.productId(),
                cardRequest.paymentSystem()
        );

        // when
        cardService.createCard(cardRequest);

        // then
        verify(kafkaProducer).sendClientCardDtoToKafka(clientCardDto);
    }
}