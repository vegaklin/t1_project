package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.accountprocessing.entity.Account;
import ru.t1.accountprocessing.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountAndPaymentDate(Account account, LocalDate paymentDate);
    List<Payment> findByAccountAndIsCreditTrue(Account account);
    List<Payment> findByAccount(Account account);
    boolean existsByAccount(Account account);
}
