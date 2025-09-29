package ru.t1.creditprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.creditprocessing.entity.PaymentRegistry;

public interface PaymentRegistryRepository extends JpaRepository<PaymentRegistry, Long> {
}
