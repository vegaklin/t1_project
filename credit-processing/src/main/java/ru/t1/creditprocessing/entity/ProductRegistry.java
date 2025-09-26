package ru.t1.creditprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product_registry")
public class ProductRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate = BigDecimal.ZERO;

    @Column(name = "open_date", nullable = false)
    private OffsetDateTime openDate;

    @OneToMany(mappedBy = "productRegistry", cascade = CascadeType.ALL)
    private List<PaymentRegistry> payments;
}