package ru.t1.creditprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.creditprocessing.entity.PaymentRegistry;

@Repository
public interface PaymentRegistryRepository extends JpaRepository<PaymentRegistry, Long> {
}
