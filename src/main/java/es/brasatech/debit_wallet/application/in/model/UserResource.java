package es.brasatech.debit_wallet.application.in.model;

import java.util.UUID;

public record UserResource(
    UUID id,
    String name,
    String email
) {}
