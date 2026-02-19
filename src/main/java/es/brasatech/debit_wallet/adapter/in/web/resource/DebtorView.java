package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.time.LocalDateTime;
import java.util.UUID;

public record DebtorView(
        UUID id,
        String name,
        String surname,
        String phone,
        String email,
        String notes,
        String address,
        LocalDateTime createdAt
) {
}

