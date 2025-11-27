package es.brasatech.debit_wallet.application.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentView(
        UUID id,
        BigDecimal amount,
        LocalDateTime date,
        String type
) {}
