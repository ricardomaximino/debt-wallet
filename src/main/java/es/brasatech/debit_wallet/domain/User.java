package es.brasatech.debit_wallet.domain;

import java.util.UUID;

public record User(
    UUID id,
    String name,
    String email
) {}
