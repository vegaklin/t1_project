package ru.t1.clientprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.clientprocessing.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
