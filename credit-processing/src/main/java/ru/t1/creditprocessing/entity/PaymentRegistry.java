package ru.t1.creditprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter@Table(name = "payment_registry")
public class PaymentRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_registry_id", nullable = false)
    private ProductRegistry productRegistry;

    @Column(name = "payment_date", nullable = false)
    private OffsetDateTime paymentDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "interest_rate_amount", nullable = false)
    private BigDecimal interestRateAmount = BigDecimal.ZERO;

    @Column(name = "debt_amount", nullable = false)
    private BigDecimal debtAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean expired = false;

    @Column(name = "payment_expiration_date")
    private OffsetDateTime paymentExpirationDate;
}