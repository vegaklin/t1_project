package ru.t1.clientprocessing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.clientprocessing.aop.annotation.HttpIncomeRequestLog;
import ru.t1.clientprocessing.dto.ClientInfoResponse;
import ru.t1.clientprocessing.dto.RegisterClientRequest;
import ru.t1.clientprocessing.dto.RegisterClientResponse;
import ru.t1.clientprocessing.service.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    @HttpIncomeRequestLog
    public ResponseEntity<RegisterClientResponse> registerClient(
            @Valid @RequestBody RegisterClientRequest registerClientRequest
    ) {
        return ResponseEntity.ok().body(clientService.registerClient(registerClientRequest));
    }

    @GetMapping("/{clientId}")
    @HttpIncomeRequestLog
    public ResponseEntity<ClientInfoResponse> getClientInfo(@PathVariable String clientId) {
        return ResponseEntity.ok().body(clientService.getClientInfo(clientId));
    }
}
