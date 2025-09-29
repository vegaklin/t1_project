package ru.t1.creditprocessing.client;

import reactor.core.publisher.Mono;
import ru.t1.creditprocessing.dto.ClientInfoResponse;

public interface ClientProcessingClient {
    Mono<ClientInfoResponse> gitClientInfo(String clientId);
}
