package es.brasatech.debit_wallet.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Debtor(
    UUID id,
    String name,
    String phone,
    String email,
    String notes,
    LocalDateTime createdAt
) {}

