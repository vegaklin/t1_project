package ru.t1.clientprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.clientprocessing.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
