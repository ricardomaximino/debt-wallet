package es.brasatech.debit_wallet.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Payment (
    UUID id,
    UUID debtId,
    BigDecimal amount,
    String date,
    PaymentType type,
    LocalDateTime createdAt
) {}
