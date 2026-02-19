package es.brasatech.debit_wallet.adapter.out.jpa.entity;

import es.brasatech.debit_wallet.domain.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "debt")
public class DebtEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private UUID walletId;
    private UUID debtorId;
    private UUID userId;
    private String name;
    private String email;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private DebtStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
