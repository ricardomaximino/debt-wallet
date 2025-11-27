package es.brasatech.debit_wallet.application.in.web.dto;

import java.util.UUID;

public record DebtorView(
        UUID id,
        String name,
        String surname,
        String email,
        String phone,
        String address
) {
}
