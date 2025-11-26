package es.brasatech.debit_wallet.application.in.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record DebtorResource(
    UUID id,
    String name,
    String phone,
    String email,
    String notes,
    LocalDateTime createdAt
) {}

