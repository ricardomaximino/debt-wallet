package es.brasatech.debit_wallet.domain.model;

import java.util.UUID;

public record Workspace(UUID id, String name, String slug, UUID ownerId) {
}
