package es.brasatech.debit_wallet.adapter.out.jpa.entity;

import es.brasatech.debit_wallet.domain.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private UUID debtId;
    private BigDecimal amount;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private LocalDateTime createdAt;
}

