package ru.t1.accountprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.accountprocessing.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
