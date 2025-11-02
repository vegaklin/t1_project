package ru.t1.clientprocessing.service;

import ru.t1.clientprocessing.dto.*;

public interface ClientProductService {
    ClientProductResponse createClientProduct(ClientProductRequest clientProductRequest);
    ClientProductResponse getClientProduct(Long id);
    ClientProductResponse updateClientProduct(Long id, ClientProductRequest clientProductRequest);
    void deleteClientProduct(Long id);
}
