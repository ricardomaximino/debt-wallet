package es.brasatech.debit_wallet.application.in.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WalletView(
    UUID id,
    String name,
    List<DebtView> debts,
    LocalDateTime createdAt,
    UUID userId
    ) {

    public WalletView(UUID userId, String name, List<DebtView> debts) {
        this(UUID.randomUUID(), name, debts, LocalDateTime.now(), userId);
    }
    public WalletView (UUID userId, String name) {
        this(userId, name, List.of());
    }

    public WalletView(WalletView walletView, List<DebtView> debts){
        this(walletView.id(), walletView.name(), debts, walletView.createdAt(), walletView.userId());
    }
}
