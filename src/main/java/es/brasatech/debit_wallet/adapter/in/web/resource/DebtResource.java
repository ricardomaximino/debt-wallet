package es.brasatech.debit_wallet.adapter.in.web.resource;

import es.brasatech.debit_wallet.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DebtResource(
        UUID id,
        PortfolioResource portfolio,
        DebtorResource debtor,
        ItemResource item,
        String name,
        BigDecimal amount,
        LocalDateTime createdAt,
        DebtStatus status,
        PaymentType paymentType,
        BigDecimal remainingBalance) {
}
