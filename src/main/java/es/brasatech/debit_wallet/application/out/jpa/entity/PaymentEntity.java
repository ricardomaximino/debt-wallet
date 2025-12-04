package es.brasatech.debit_wallet.application.out.jpa.entity;

import es.brasatech.debit_wallet.domain.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String date;
    private PaymentType type;
    private LocalDateTime createdAt;
}
