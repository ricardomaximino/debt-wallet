package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.util.UUID;

public record UserResource(
    UUID id,
    String name,
    String email
) {}

