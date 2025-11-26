package es.brasatech.debit_wallet.application.in.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioResource(
    UUID id,
    String name,
    String description,
    LocalDateTime createdAt
) {}
