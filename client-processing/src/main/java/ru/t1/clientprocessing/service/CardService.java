package ru.t1.clientprocessing.service;

import ru.t1.clientprocessing.dto.CardRequest;

public interface CardService {
    void createCard(CardRequest cardRequest);
}
