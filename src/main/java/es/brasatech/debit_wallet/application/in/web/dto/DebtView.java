package es.brasatech.debit_wallet.application.in.web.dto;

import es.brasatech.debit_wallet.domain.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DebtView(UUID id, String description, UUID walletId, UUID debtorId, String name, String email, BigDecimal value, PaymentType paymentType, List<PaymentView> payments, LocalDateTime createdAt) {

    public DebtView(UUID id, String description, UUID walletId,BigDecimal amount, PaymentType paymentType, List<PaymentView> payments, DebtorView debtor) {
        this(id, description, walletId, debtor.id(), debtor.name(), debtor.email(), amount, paymentType, payments, LocalDateTime.now());
    }

    public BigDecimal amount() {
        return value;
    }

    public BigDecimal paid() {
        return payments.stream().map(PaymentView::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal outstanding() {
        return value.subtract(paid());
    }
}
