package es.brasatech.debit_wallet.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Portfolio (
    UUID id,
    String name,
    String description,
    LocalDateTime createdAt
) {}
