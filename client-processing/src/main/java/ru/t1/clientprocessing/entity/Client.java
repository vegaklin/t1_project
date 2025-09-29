package ru.t1.clientprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.t1.clientprocessing.model.DocumentType;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, unique = true, length = 12)
    private String clientId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 20)
    private DocumentType documentType;

    @Column(name = "document_id", nullable = false, length = 50)
    private String documentId;

    @Column(name = "document_prefix", length = 10)
    private String documentPrefix;

    @Column(name = "document_suffix", length = 10)
    private String documentSuffix;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientProduct> clientProducts;
}
