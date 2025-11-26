package es.brasatech.debit_wallet.application.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "debtor")
public class DebtorEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String phone;
    private String email;
    @Column(columnDefinition = "text")
    private String notes;
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}

