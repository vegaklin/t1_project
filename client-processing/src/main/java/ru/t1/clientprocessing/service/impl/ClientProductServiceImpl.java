package ru.t1.clientprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.clientprocessing.dto.*;
import ru.t1.clientprocessing.entity.Client;
import ru.t1.clientprocessing.entity.ClientProduct;
import ru.t1.clientprocessing.entity.Product;
import ru.t1.clientprocessing.exception.*;
import ru.t1.clientprocessing.kafka.ClientCreditProductsKafkaProducer;
import ru.t1.clientprocessing.kafka.ClientProductsKafkaProducer;
import ru.t1.clientprocessing.model.ProductKey;
import ru.t1.clientprocessing.repository.ClientProductRepository;
import ru.t1.clientprocessing.repository.ClientRepository;
import ru.t1.clientprocessing.repository.ProductRepository;
import ru.t1.clientprocessing.service.ClientProductService;

@Service
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private final ClientProductRepository clientProductRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    private final ClientProductsKafkaProducer clientProductsKafkaProducer;
    private final ClientCreditProductsKafkaProducer clientCreditProductsKafkaProducer;

    @Override
    @Transactional
    public ClientProductResponse createClientProduct(ClientProductRequest clientProductRequest) {
        Client client = clientRepository.findById(clientProductRequest.clientId())
                .orElseThrow(() -> new NoClientException("Client not found"));

        Product product = productRepository.findById(clientProductRequest.productId())
                .orElseThrow(() -> new NoProductException("Product not found"));

        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setClient(client);
        clientProduct.setProduct(product);
        clientProduct.setOpenDate(clientProductRequest.openDate());
        clientProduct.setCloseDate(clientProductRequest.closeDate());
        clientProduct.setStatus(clientProductRequest.status());
        clientProduct = clientProductRepository.save(clientProduct);

        sentToKafka(clientProduct, clientProductRequest);

        return toResponse(clientProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientProductResponse getClientProduct(Long id) {
        ClientProduct clientProduct = clientProductRepository.findById(id)
                .orElseThrow(() -> new NoClientProductException("ClientProduct with id " + id + " not found"));
        return toResponse(clientProduct);
    }

    @Override
    @Transactional
    public ClientProductResponse updateClientProduct(Long id, ClientProductRequest clientProductRequest) {
        ClientProduct clientProduct = clientProductRepository.findById(id)
                .orElseThrow(() -> new NoClientProductException("ClientProduct with id " + id + " not found"));

        Client client = clientRepository.findById(clientProductRequest.clientId())
                .orElseThrow(() -> new NoClientException("Client not found"));

        Product product = productRepository.findById(clientProductRequest.productId())
                .orElseThrow(() -> new NoProductException("Product not found"));

        clientProduct.setClient(client);
        clientProduct.setProduct(product);
        clientProduct.setOpenDate(clientProductRequest.openDate());
        clientProduct.setCloseDate(clientProductRequest.closeDate());
        clientProduct.setStatus(clientProductRequest.status());

        ClientProduct updated = clientProductRepository.save(clientProduct);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteClientProduct(Long id) {
        if (!clientProductRepository.existsById(id)) {
            throw new NoClientProductException("ClientProduct with id " + id + " not found");
        }
        clientProductRepository.deleteById(id);
    }

    private void sentToKafka(ClientProduct clientProduct, ClientProductRequest clientProductRequest) {
        ProductKey key = clientProduct.getProduct().getKey();

        if (key == ProductKey.DC || key == ProductKey.CC || key == ProductKey.NS || key == ProductKey.PENS) {
            if (clientProductRequest.isRecalc() == null) {
                throw new NoProductDataException("Requested isRecalc is not specified");
            }
            ClientProductDto clientProductDto = new ClientProductDto(
                    clientProduct.getClient().getClientId(),
                    clientProduct.getProduct().getProductId(),
                    clientProduct.getStatus(),
                    clientProductRequest.isRecalc()
            );
            clientProductsKafkaProducer.sendClientProductDtoToKafka(clientProductDto);
        } else if (key == ProductKey.IPO || key == ProductKey.PC || key == ProductKey.AC) {
            if (clientProductRequest.amount() == null
                    || clientProductRequest.monthCount() == null) {
                throw new NoCreditDataException("Requested amount or/and monthCount is not specified");
            }

            ClientCreditProductDto clientCreditProductDto = new ClientCreditProductDto(
                    clientProduct.getClient().getClientId(),
                    clientProduct.getProduct().getProductId(),
                    clientProductRequest.amount(),
                    clientProductRequest.monthCount()
            );
            clientCreditProductsKafkaProducer.sendClientCardDtoToKafka(clientCreditProductDto);
        }
    }

    private ClientProductResponse toResponse(ClientProduct product) {
        return new ClientProductResponse(
                product.getId(),
                product.getProduct().getId(),
                product.getClient().getId(),
                product.getOpenDate(),
                product.getCloseDate(),
                product.getStatus()
        );
    }
}
