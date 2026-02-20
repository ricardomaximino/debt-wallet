package es.brasatech.debit_wallet.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Debt(
        UUID id,
        UUID walletId,
        UUID debtorId,
        UUID userId,
        UUID workspaceId,
        String name,
        String email,
        String description,
        BigDecimal amount,
        LocalDateTime createdAt,
        DebtStatus status,
        PaymentType paymentType,
        List<Item> items) {
}
