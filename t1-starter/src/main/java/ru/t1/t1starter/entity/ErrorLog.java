package ru.t1.t1starter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.t1.t1starter.model.LogType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "error_log")
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "microservice_name", nullable = false)
    private String microserviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LogType type;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
