package es.brasatech.debit_wallet.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Item(
    UUID id,
    String name,
    LocalDate date,
    String category,
    LocalDateTime createdAt
) {}
