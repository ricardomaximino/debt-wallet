package es.brasatech.debit_wallet.adapter.in.web.resource;

import es.brasatech.debit_wallet.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DebtView(
        UUID id,
        String description,
        UUID walletId,
        UUID clientId,
        String name,
        String email,
        BigDecimal amount,
        PaymentType paymentType,
        List<PaymentView> payments,
        LocalDateTime createdAt) {

    public DebtView {
        payments = payments == null ? List.of() : payments;
    }

    public DebtView(DebtView debtView, List<PaymentView> payments) {
        this(debtView.id(), debtView.description(), debtView.walletId(), debtView.clientId(), debtView.name(),
                debtView.email(), debtView.amount(), debtView.paymentType(), payments, debtView.createdAt());
    }

    public BigDecimal paid() {
        return payments.stream().map(PaymentView::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal outstanding() {
        return amount.subtract(paid());
    }

    public boolean isFullyPaid() {
        return outstanding().compareTo(BigDecimal.ZERO) <= 0;
    }

    public double getProgressFactor() {
        if (amount.compareTo(BigDecimal.ZERO) == 0)
            return 0;
        return paid().multiply(new java.math.BigDecimal(100)).divide(amount, 2, java.math.RoundingMode.HALF_UP)
                .doubleValue();
    }
}
