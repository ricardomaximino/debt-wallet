package es.brasatech.debit_wallet.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Wallet(UUID id, String name, LocalDateTime createdAt, UUID userId, UUID workspaceId) {
}
