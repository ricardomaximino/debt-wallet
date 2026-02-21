package es.brasatech.debit_wallet.domain.model;

import java.util.Set;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String firstName,
        String middleName,
        String surname,
        java.time.LocalDate birthday,
        String email,
        String username,
        String password,
        String profilePicturePath,
        boolean enabled,
        PlanRole planRole,
        Set<UserRole> roles,
        Set<UUID> workspaceIds,
        java.time.LocalDateTime createdAt) {
}
