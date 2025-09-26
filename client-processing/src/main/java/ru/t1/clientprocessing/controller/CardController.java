package ru.t1.clientprocessing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.clientprocessing.dto.CardRequest;
import ru.t1.clientprocessing.service.CardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService clientProductService;

    @PostMapping("/create")
    public void createCard(
            @Valid @RequestBody CardRequest cardRequest
    ) {
        clientProductService.createCard(cardRequest);
    }
}