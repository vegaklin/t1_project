package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.accountprocessing.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}