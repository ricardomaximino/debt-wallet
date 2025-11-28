package es.brasatech.debit_wallet.application.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentView(
        UUID id,
        UUID debitId,
        BigDecimal amount,
        LocalDateTime date,
        String type,
        LocalDateTime createdAt
) {

    public PaymentView(UUID id, UUID debitId, BigDecimal amount, LocalDateTime date, String type){
        this(id, debitId, amount, date, type, LocalDateTime.now());
    }
}
