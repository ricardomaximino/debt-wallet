package es.brasatech.debit_wallet.adapter.in.web.resource;

import java.math.BigDecimal;
import java.util.List;

public record DebtWalletView(BigDecimal total, int activeWallets, List<WalletView> wallets) {
}

