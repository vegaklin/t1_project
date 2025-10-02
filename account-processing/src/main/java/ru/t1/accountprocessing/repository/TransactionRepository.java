package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.accountprocessing.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Long> countByCard_CardIdAndTimestampAfter(String cardId, LocalDateTime from);
}