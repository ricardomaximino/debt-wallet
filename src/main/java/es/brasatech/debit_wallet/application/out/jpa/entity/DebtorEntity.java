package es.brasatech.debit_wallet.application.out.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "debtor")
public class DebtorEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String phone;
    private String email;
    @Column(columnDefinition = "text")
    private String notes;
    private LocalDateTime createdAt;
}

