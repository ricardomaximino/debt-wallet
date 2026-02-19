package es.brasatech.debit_wallet.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Payment(
        UUID id,
        UUID debtId,
        BigDecimal amount,
        LocalDateTime date,
        PaymentType type,
        LocalDateTime createdAt) {
}
