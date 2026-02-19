package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResource(
    UUID id,
    DebtResource debt,
    BigDecimal amount,
    LocalDateTime date,
    String method,
    String note
) {}

