package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.accountprocessing.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
}
