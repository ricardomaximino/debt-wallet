package es.brasatech.debit_wallet.application.in.model;

import es.brasatech.debit_wallet.domain.DebtStatus;
import es.brasatech.debit_wallet.domain.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DebtResource(
    UUID id,
    PortfolioResource portfolio,
    DebtorResource debtor,
    ItemResource item,
    String name,
    BigDecimal value,
    LocalDateTime createdAt,
    DebtStatus status,
    PaymentType paymentType,
    BigDecimal remainingBalance
) {}
