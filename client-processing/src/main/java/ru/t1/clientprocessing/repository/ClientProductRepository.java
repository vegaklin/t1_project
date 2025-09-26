package ru.t1.clientprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.clientprocessing.entity.ClientProduct;

public interface ClientProductRepository extends JpaRepository<ClientProduct, Long> {
}
