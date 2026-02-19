package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ItemResource(
    UUID id,
    String name,
    LocalDate date,
    String category,
    LocalDateTime createdAt
) {}

