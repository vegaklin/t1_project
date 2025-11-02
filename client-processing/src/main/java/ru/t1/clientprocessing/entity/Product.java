package ru.t1.clientprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.t1.clientprocessing.model.ProductKey;

import java.time.LocalDate;
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
    private LocalDate createDate;

    @Column(name = "product_id", unique = true, length = 100)
    private String productId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ClientProduct> clientProducts;
}