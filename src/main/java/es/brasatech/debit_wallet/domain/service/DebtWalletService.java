package es.brasatech.debit_wallet.domain.service;

import es.brasatech.debit_wallet.application.in.web.dto.DebtView;
import es.brasatech.debit_wallet.application.in.web.dto.DebtorView;
import es.brasatech.debit_wallet.application.in.web.dto.PaymentView;
import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.domain.User;

import java.util.List;
import java.util.UUID;

public interface DebtWalletService {

    UUID getLoggedUseId();

    User getUserById(UUID userId);

    WalletView createWalletView(UUID userId, String name);

    DebtorView createDebtorView(String name, String email);

    DebtView crateDebtView(UUID userId, DebtView  debtView);

    PaymentView registerPayment(UUID userId, PaymentView paymentView);

    // view

    List<WalletView> getWalletViews(UUID userId);
}
