package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.accountprocessing.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
