package ru.t1.clientprocessing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.clientprocessing.dto.ProductRequest;
import ru.t1.clientprocessing.dto.ProductResponse;
import ru.t1.clientprocessing.entity.Product;
import ru.t1.clientprocessing.exception.NoProductException;
import ru.t1.clientprocessing.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.name());
        product.setKey(productRequest.key());
        product.setCreateDate(productRequest.createDate());

        product = productRepository.save(product);
        product.setProductId(product.getKey().toString() + product.getId());

        product = productRepository.save(product);
        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoProductException("Product with id " + id + " not found"));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoProductException("Product with id " + id + " not found"));

        product.setName(productRequest.name());
        product.setKey(productRequest.key());
        product.setCreateDate(productRequest.createDate());
        product.setProductId(product.getKey().toString() + product.getId());

        Product updated = productRepository.save(product);
        return toResponse(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoProductException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getKey(),
                product.getCreateDate(),
                product.getProductId()
        );
    }
}
