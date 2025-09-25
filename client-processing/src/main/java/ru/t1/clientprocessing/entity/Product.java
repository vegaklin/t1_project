package ru.t1.clientprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "key", nullable = false, length = 10)
    private ProductKey key;

    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @Column(name = "product_id", nullable = false, unique = true, length = 100)
    private String productId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ClientProduct> clientProducts;

    public enum ProductKey {
        DC, CC, AC, IPO, PC, PENS, NS, INS, BS
    }
}