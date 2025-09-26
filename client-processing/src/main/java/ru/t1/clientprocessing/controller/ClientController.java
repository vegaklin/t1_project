package ru.t1.clientprocessing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.clientprocessing.dto.RegisterClientRequest;
import ru.t1.clientprocessing.dto.RegisterClientResponse;
import ru.t1.clientprocessing.service.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<RegisterClientResponse> registerClient(
            @Valid @RequestBody RegisterClientRequest registerClientRequest
    ) {
        return ResponseEntity.ok().body(clientService.registerClient(registerClientRequest));
    }}
