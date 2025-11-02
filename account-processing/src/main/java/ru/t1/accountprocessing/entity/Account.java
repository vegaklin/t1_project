package ru.t1.accountprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.t1.accountprocessing.model.ClientStatus;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        name = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"client_id", "product_id"})
        }
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, length = 12)
    private String clientId;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate = BigDecimal.ZERO;

    @Column(name = "is_recalc", nullable = false)
    private Boolean isRecalc = false;

    @Column(name = "card_exist", nullable = false)
    private Boolean cardExist = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}