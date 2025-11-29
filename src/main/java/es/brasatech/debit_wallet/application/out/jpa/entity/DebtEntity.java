package es.brasatech.debit_wallet.application.out.jpa.entity;

import es.brasatech.debit_wallet.domain.DebtStatus;
import es.brasatech.debit_wallet.domain.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "debt")
public class DebtEntity {
    @Id @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private UUID userId;

    private UUID portfolioId;

    private UUID debtorId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private String name;

    private String note;

    private BigDecimal amount;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private DebtStatus status = DebtStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.FLEXIBLE;
}
