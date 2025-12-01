package es.brasatech.debit_wallet.application.in.web.dto;

import es.brasatech.debit_wallet.domain.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentView(
        UUID id,
        UUID debitId,
        BigDecimal amount,
        String date,
        PaymentType type,
        LocalDateTime createdAt
) {

}
