package es.brasatech.debit_wallet.application.in.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WalletView(UUID id, String name, List<DebtView> debts, LocalDateTime createdAt) {
    public WalletView(UUID id, String name, List<DebtView> debts) {
        this(id, name, debts, LocalDateTime.now());
    }
    public WalletView (String name) {
        this(UUID.randomUUID(), name, List.of());

    }
}
