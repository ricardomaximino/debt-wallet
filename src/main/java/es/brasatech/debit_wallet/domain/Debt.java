package es.brasatech.debit_wallet.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record Debt(
    UUID id,
    UUID walletId,
    UUID debtorId,
    UUID userId,
    String name,
    String email,
    String description,
    BigDecimal amount,
    LocalDateTime createdAt,
    DebtStatus status,
    PaymentType paymentType,
    List<Payment> payments
) {

    public Debt registePayment(Payment payment){
        var list = Stream.concat(payments.stream(), Stream.of(payment)).toList();
        return new Debt(id, walletId, debtorId, userId, name, email, description, amount, createdAt, status, paymentType, list);
    }
}
