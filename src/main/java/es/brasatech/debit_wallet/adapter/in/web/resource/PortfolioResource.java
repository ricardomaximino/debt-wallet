package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioResource(
    UUID id,
    String name,
    String description,
    LocalDateTime createdAt
) {}

