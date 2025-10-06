package ru.t1.clientprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.clientprocessing.aop.annotation.Cached;
import ru.t1.clientprocessing.aop.annotation.Metric;
import ru.t1.clientprocessing.dto.ClientInfoResponse;
import ru.t1.clientprocessing.dto.RegisterClientRequest;
import ru.t1.clientprocessing.dto.RegisterClientResponse;
import ru.t1.clientprocessing.entity.BlacklistRegistry;
import ru.t1.clientprocessing.entity.Client;
import ru.t1.clientprocessing.entity.User;
import ru.t1.clientprocessing.exception.BlacklistedClientException;
import ru.t1.clientprocessing.exception.NoClientException;
import ru.t1.clientprocessing.repository.BlacklistRegistryRepository;
import ru.t1.clientprocessing.repository.ClientRepository;
import ru.t1.clientprocessing.repository.UserRepository;
import ru.t1.clientprocessing.service.ClientService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final BlacklistRegistryRepository blacklistRegistryRepository;

    @Override
    @Metric
    @Transactional
    public RegisterClientResponse registerClient(RegisterClientRequest registerClientRequest) {
        Optional<BlacklistRegistry> blacklistRegistryOptional = blacklistRegistryRepository.findByDocumentTypeAndDocumentId(
                registerClientRequest.documentType(), registerClientRequest.documentId()
        );
        if (blacklistRegistryOptional.isPresent()) {
            throw new BlacklistedClientException("Client in blacklist: " + blacklistRegistryOptional.get().getReason());
        }

        User user = getUser(registerClientRequest);
        user = userRepository.save(user);

        Client client = getClient(registerClientRequest, user);
        client = clientRepository.save(client);

        return new RegisterClientResponse(
                client.getId(),
                user.getId()
        );
    }

    @Override
    @Metric
    @Cached
    @Transactional(readOnly = true)
    public ClientInfoResponse getClientInfo(String clientId) {
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new NoClientException("ClientProduct with clientId " + clientId + " not found"));

        String fullName = String.format("%s %s %s",
                client.getLastName(),
                client.getFirstName(),
                client.getMiddleName() != null ? client.getMiddleName() : ""
        ).trim();

        return new ClientInfoResponse(fullName, client.getDocumentId());
    }

    private User getUser(RegisterClientRequest registerClientRequest) {
        User user = new User();
        user.setLogin(registerClientRequest.login());
        user.setPassword(registerClientRequest.password()); // TODO: security
        user.setEmail(registerClientRequest.email());
        return user;
    }

    private Client getClient(RegisterClientRequest registerClientRequest, User user) {
        Client client = new Client();
        client.setClientId(registerClientRequest.clientId());
        client.setFirstName(registerClientRequest.firstName());
        client.setMiddleName(registerClientRequest.middleName());
        client.setLastName(registerClientRequest.lastName());
        client.setDateOfBirth(registerClientRequest.dateOfBirth());
        client.setDocumentType(registerClientRequest.documentType());
        client.setDocumentId(registerClientRequest.documentId());
        client.setDocumentPrefix(registerClientRequest.documentPrefix());
        client.setDocumentSuffix(registerClientRequest.documentSuffix());
        client.setUser(user);
        return client;
    }
}
