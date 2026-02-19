package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WalletView(
        UUID id,
        String name,
        List<DebtView> debts,
        LocalDateTime createdAt,
        UUID userId) {

    public WalletView {
        debts = debts == null ? List.of() : debts;
    }

    public WalletView(WalletView walletView, List<DebtView> debts) {
        this(walletView.id(), walletView.name(), debts, walletView.createdAt(), walletView.userId());
    }

    public java.math.BigDecimal totalOwed() {
        return debts.stream().map(DebtView::outstanding).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}
