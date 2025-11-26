package es.brasatech.debit_wallet.application.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id @GeneratedValue private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "debt_id", nullable = false)
    private DebtEntity debt;

    private BigDecimal amount;
    private LocalDateTime date = LocalDateTime.now();
    private String method;
    @Column(columnDefinition = "text") private String note;
    // getters/setters
}
