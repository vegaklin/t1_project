package ru.t1.clientprocessing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.t1.clientprocessing.dto.ProductRequest;
import ru.t1.clientprocessing.dto.ProductResponse;
import ru.t1.clientprocessing.service.ProductService;
import ru.t1.t1starter.annotation.HttpIncomeRequestLog;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAuthority('MASTER')")
    @PostMapping
    @HttpIncomeRequestLog
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok().body(productService.createProduct(productRequest));
    }

    @GetMapping("/{id}")
    @HttpIncomeRequestLog
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @PreAuthorize("hasAuthority('MASTER') or hasAuthority('GRAND_EMPLOYEE')")
    @PutMapping("/{id}")
    @HttpIncomeRequestLog
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok().body(productService.updateProduct(id, productRequest));
    }

    @PreAuthorize("hasAuthority('MASTER') or hasAuthority('GRAND_EMPLOYEE')")
    @DeleteMapping("/{id}")
    @HttpIncomeRequestLog
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}