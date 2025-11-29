package es.brasatech.debit_wallet.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Debt(
    UUID id,
    UUID walletId,
    UUID debtorId,
    UUID userId,
    Item item,
    String name,
    BigDecimal amount,
    LocalDateTime createdAt,
    DebtStatus status,
    PaymentType paymentType,
    BigDecimal remainingBalance,
    List<Operation> operations
) {}
