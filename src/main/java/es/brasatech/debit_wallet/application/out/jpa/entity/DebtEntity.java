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
    @Id @GeneratedValue private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private PortfolioEntity portfolio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private DebtorEntity debtor;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private String name;
    @Column(columnDefinition = "text") private String note;
    private BigDecimal value;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private DebtStatus status = DebtStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.FLEXIBLE;

    private BigDecimal remainingBalance;

    // getters/setters
}
