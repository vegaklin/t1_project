package ru.t1.clientprocessing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.clientprocessing.dto.ClientProductRequest;
import ru.t1.clientprocessing.dto.ClientProductResponse;
import ru.t1.clientprocessing.service.ClientProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client_products")
public class ClientProductsController {

    private final ClientProductService clientProductService;

    @PostMapping
    public ResponseEntity<ClientProductResponse> createClientProduct(
            @Valid @RequestBody ClientProductRequest clientProductRequest
    ) {
        return ResponseEntity.ok().body(clientProductService.createClientProduct(clientProductRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientProductResponse> getClientProduct(@PathVariable Long id) {
        return ResponseEntity.ok().body(clientProductService.getClientProduct(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientProductResponse> updateClientProduct(
            @PathVariable Long id,
            @Valid @RequestBody ClientProductRequest clientProductRequest
    ) {
        return ResponseEntity.ok().body(clientProductService.updateClientProduct(id, clientProductRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        clientProductService.deleteClientProduct(id);
    }
}
