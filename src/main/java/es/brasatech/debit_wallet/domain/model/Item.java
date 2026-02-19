package es.brasatech.debit_wallet.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Item(UUID id, UUID debtId, String name, BigDecimal amount) {
}
