package ru.t1.clientprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.clientprocessing.dto.ClientInfoResponse;
import ru.t1.clientprocessing.dto.RegisterClientRequest;
import ru.t1.clientprocessing.dto.RegisterClientResponse;
import ru.t1.clientprocessing.entity.BlacklistRegistry;
import ru.t1.clientprocessing.entity.Client;
import ru.t1.clientprocessing.entity.Role;
import ru.t1.clientprocessing.entity.User;
import ru.t1.clientprocessing.exception.BlacklistedClientException;
import ru.t1.clientprocessing.exception.NoClientException;
import ru.t1.clientprocessing.model.UserRole;
import ru.t1.clientprocessing.repository.BlacklistRegistryRepository;
import ru.t1.clientprocessing.repository.ClientRepository;
import ru.t1.clientprocessing.repository.RoleRepository;
import ru.t1.clientprocessing.repository.UserRepository;
import ru.t1.clientprocessing.service.ClientService;
import ru.t1.t1starter.annotation.Cached;
import ru.t1.t1starter.annotation.LogDatasourceError;
import ru.t1.t1starter.annotation.Metric;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final BlacklistRegistryRepository blacklistRegistryRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Metric
    @LogDatasourceError
    @Transactional(noRollbackFor = BlacklistedClientException.class)
    public RegisterClientResponse registerClient(RegisterClientRequest registerClientRequest) {
        User user = getUser(registerClientRequest);

        Optional<BlacklistRegistry> blacklistRegistryOptional = blacklistRegistryRepository.findByDocumentTypeAndDocumentId(
                registerClientRequest.documentType(), registerClientRequest.documentId()
        );
        if (blacklistRegistryOptional.isPresent()) {
            Role blockedClientRole = roleRepository.findByName(UserRole.BLOCKED_CLIENT)
                    .orElseGet(() -> roleRepository.save(createRole(UserRole.BLOCKED_CLIENT)));

            user.setRoles(Set.of(blockedClientRole));
            userRepository.save(user);

            throw new BlacklistedClientException("Client in blacklist: " + blacklistRegistryOptional.get().getReason());
        }

        Role currentClientRole = roleRepository.findByName(UserRole.CURRENT_CLIENT)
                .orElseGet(() -> roleRepository.save(createRole(UserRole.CURRENT_CLIENT)));

        user.setRoles(Set.of(currentClientRole));
        userRepository.save(user);

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
    @LogDatasourceError
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

    private Role createRole(UserRole userRole) {
        Role role = new Role();
        role.setName(userRole);
        return role;
    }

    private User getUser(RegisterClientRequest registerClientRequest) {
        User user = new User();
        user.setLogin(registerClientRequest.login());
        user.setPassword(passwordEncoder.encode(registerClientRequest.password()));
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
