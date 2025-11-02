package ru.t1.accountprocessing.service;

import ru.t1.accountprocessing.dto.ClientCardDto;

public interface CardService {
    void createCard(ClientCardDto clientCardDto);
}
