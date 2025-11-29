package es.brasatech.debit_wallet.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Wallet(
    UUID id,
    String name,
    LocalDateTime createdAt,
    UUID userId
) {

    public Wallet(UUID userId, String name) {
        this(UUID.randomUUID(), name, LocalDateTime.now(), userId);
    }
}
