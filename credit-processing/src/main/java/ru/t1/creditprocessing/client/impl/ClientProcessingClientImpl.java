package ru.t1.creditprocessing.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.t1.creditprocessing.client.ClientProcessingClient;
import ru.t1.creditprocessing.dto.ClientErrorResponse;
import ru.t1.creditprocessing.dto.ClientInfoResponse;
import ru.t1.creditprocessing.exception.ClientProcessingClientException;
import ru.t1.t1starter.annotation.HttpOutcomeRequestLog;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientProcessingClientImpl implements ClientProcessingClient {

    private final WebClient clientProcessingWebClient;

    @Override
    @HttpOutcomeRequestLog
    public Mono<ClientInfoResponse> gitClientInfo(String clientId) {
        return clientProcessingWebClient
                .get()
                .uri("/clients/{clientId}", clientId)
                .exchangeToMono(response -> handleResponse(response, ClientInfoResponse.class));
    }

    private <T> Mono<T> handleResponse(ClientResponse response, Class<T> responseType) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(responseType)
                    .doOnSuccess(result -> log.info("Successfully response: {}", result));
        } else {
            return response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(error -> {
                        log.error("Client error: {}", error.message());
                        return Mono.error(new ClientProcessingClientException(error.message()));
                    });
        }
    }
}
