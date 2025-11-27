package es.brasatech.debit_wallet.application.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record WalletView(BigDecimal total, List<DebtView> debts) {
}
