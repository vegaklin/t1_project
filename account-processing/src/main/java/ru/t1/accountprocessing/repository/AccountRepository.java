package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.accountprocessing.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByClientIdAndProductId(String clientId, String productId);
}
