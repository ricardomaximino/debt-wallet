package es.brasatech.debit_wallet.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Debtor(
        UUID id,
        String name,
        String surname,
        String nif,
        String email,
        String phone,
        String address,
        LocalDateTime createdAt) {
}
