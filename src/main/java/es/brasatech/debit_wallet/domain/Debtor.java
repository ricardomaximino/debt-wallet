package es.brasatech.debit_wallet.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Debtor(
    UUID id,
    String name,
    String surname,
    String phone,
    String email,
    String notes,
    String address,
    LocalDateTime createdAt
) {
    public Debtor(String name, String email) {
        this(UUID.randomUUID(), name, null,null, email, null, null,LocalDateTime.now());
    }
}

