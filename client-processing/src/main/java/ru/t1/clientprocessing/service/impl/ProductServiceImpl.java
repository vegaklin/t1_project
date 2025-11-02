package ru.t1.clientprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.clientprocessing.dto.ProductRequest;
import ru.t1.clientprocessing.dto.ProductResponse;
import ru.t1.clientprocessing.entity.Product;
import ru.t1.clientprocessing.exception.NoProductException;
import ru.t1.clientprocessing.repository.ProductRepository;
import ru.t1.clientprocessing.service.ProductService;
import ru.t1.t1starter.annotation.Cached;
import ru.t1.t1starter.annotation.LogDatasourceError;
import ru.t1.t1starter.annotation.Metric;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Metric
    @LogDatasourceError
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

    @Override
    @Metric
    @Cached
    @LogDatasourceError
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoProductException("Product with id " + id + " not found"));
        return toResponse(product);
    }

    @Override
    @Metric
    @LogDatasourceError
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

    @Override
    @Metric
    @LogDatasourceError
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