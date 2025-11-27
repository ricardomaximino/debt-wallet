package es.brasatech.debit_wallet.application.in.web.dto;

import es.brasatech.debit_wallet.domain.PaymentType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DebtView(
        UUID id,
        String description,
        UUID debtorId,
        String name,
        String email,
        BigDecimal amount,
        BigDecimal paid,
        BigDecimal outstanding,
        PaymentType paymentType,
        List<PaymentView> payments) {

    public DebtView(
            UUID id,
            String description,
            BigDecimal amount,
            PaymentType paymentType,
            List<PaymentView> payments,
            DebtorView debtor) {
        var paid = payments.stream().map(PaymentView::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        var outstanding = amount.subtract(paid);
        this(id, description, debtor.id(), debtor.name(), debtor.email(), amount, paid, outstanding, paymentType, payments);
    }
}
