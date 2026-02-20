package es.brasatech.debit_wallet.application.port_in;

import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
import es.brasatech.debit_wallet.adapter.in.web.resource.ClientView;
import es.brasatech.debit_wallet.adapter.in.web.resource.PaymentView;
import es.brasatech.debit_wallet.adapter.in.web.resource.WalletView;
import es.brasatech.debit_wallet.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface DebtWalletService {

    UUID getLoggedUseId();

    User getUserById(UUID userId);

    WalletView createWalletView(UUID userId, String name);

    ClientView createClientView(String name, String email);

    DebtView crateDebtView(UUID userId, DebtView debtView);

    PaymentView registerPayment(UUID userId, PaymentView paymentView);

    // view

    List<WalletView> getWalletViews(UUID userId);

    List<ClientView> searchClientViews(String query);
}
