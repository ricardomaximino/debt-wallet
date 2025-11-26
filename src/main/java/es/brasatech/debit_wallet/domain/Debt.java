package es.brasatech.debit_wallet.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Debt(
    UUID id,
    Portfolio portfolio,
    Debtor debtor,
    Item item,
    String name,
    BigDecimal value,
    LocalDateTime createdAt,
    DebtStatus status,
    PaymentType paymentType,
    BigDecimal remainingBalance
) {}
