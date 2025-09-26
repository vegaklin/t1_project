package ru.t1.clientprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.clientprocessing.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
