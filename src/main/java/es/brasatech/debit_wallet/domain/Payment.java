package es.brasatech.debit_wallet.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Payment (
    UUID id,
    Debt debt,
    BigDecimal amount,
    LocalDateTime date,
    String method,
    String note
) {}
