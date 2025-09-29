package ru.t1.creditprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.creditprocessing.entity.ProductRegistry;

import java.util.List;

public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {
    List<ProductRegistry> findByClientId(String clientId);
}
