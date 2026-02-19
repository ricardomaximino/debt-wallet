package es.brasatech.debit_wallet.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private UUID debtId;
    private String name;
    private BigDecimal amount;
}
