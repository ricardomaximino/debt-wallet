package es.brasatech.debit_wallet.application.port_out;

import es.brasatech.debit_wallet.domain.model.*;

import java.util.List;
import java.util.UUID;

public interface WalletPersistencePort {
    Wallet saveWallet(Wallet wallet);

    List<Wallet> findAllWalletsByUserId(UUID userId);

    Debt saveDebt(Debt debt);

    List<Debt> findDebtsByUserId(UUID userId);

    List<Debt> findDebtsByWalletId(UUID walletId);

    Payment savePayment(Payment payment);

    List<Payment> findPaymentsByDebtId(UUID debtId);

    List<User> searchClients(String query);

    java.util.Optional<User> findUserByUsername(String username);

    java.util.Optional<User> findUserById(UUID userId);

    java.util.Optional<Workspace> findWorkspaceBySlug(String slug);

    User saveUser(User user);

    boolean existsByUsername(String username);

    Workspace saveWorkspace(Workspace workspace);
}
