package es.brasatech.debit_wallet.domain.model;

import java.util.Set;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String email,
        String username,
        String password,
        boolean active,
        PlanRole planRole,
        Set<UserRole> roles,
        Set<UUID> workspaceIds) {
}
