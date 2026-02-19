package es.brasatech.debit_wallet.domain.model;

import java.util.UUID;

public record User(UUID id, String name, String email, PlanRole planRole) {
}
