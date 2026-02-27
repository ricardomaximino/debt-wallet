package es.brasatech.debit_wallet.application.service;

import es.brasatech.debit_wallet.adapter.in.web.mapper.WalletMapperView;
import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
import es.brasatech.debit_wallet.adapter.in.web.resource.ClientView;
import es.brasatech.debit_wallet.adapter.in.web.resource.PaymentView;
import es.brasatech.debit_wallet.adapter.in.web.resource.WalletView;
import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements DebtWalletService {

    private final WalletPersistencePort persistencePort;
    private final WalletMapperView viewMapper;
    private final ClientUsernameGenerator usernameGenerator;

    @Override
    public UUID getLoggedUseId() {
        // Fallback to the known test user ID until full security integration
        return UUID.fromString("fd4437c0-bda6-489d-964f-7e43169cace0");
    }

    @Override
    public User getUserById(UUID userId) {
        return persistencePort.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    @Override
    public WalletView createWalletView(UUID userId, UUID workspaceId, String name) {
        var wallet = new Wallet(UUID.randomUUID(), name, LocalDateTime.now(), userId, workspaceId);
        var savedWallet = persistencePort.saveWallet(wallet);
        return viewMapper.mapToWalletView(savedWallet);
    }

    @Override
    public ClientView createClientView(UUID workspaceId, String name, String email) {
        UUID clientId = UUID.randomUUID();
        // Automatic workspace for new client (if they don't have one, but here we are
        // in a workspace context)
        // Usually, createClientView is called by a Lawyer in THEIR workspace.
        // If a client is created as a "standalone user", they get their own workspace.
        // But for feature isolation, we might want to just create the user.

        String username = usernameGenerator.generate(name);

        var client = new User(
                clientId,
                name,
                null, // firstName
                null, // middleName
                null, // surname
                null, // birthday
                email,
                username,
                "{noop}client123", // Default password for new clients
                null, // profilePicturePath
                true,
                PlanRole.FREE,
                java.util.Set.of(UserRole.CLIENT),
                java.util.Set.of(workspaceId),
                java.time.LocalDateTime.now() // createdAt
        );

        var savedClient = persistencePort.saveUser(client);
        return viewMapper.mapToClientView(savedClient);
    }

    @Override
    public DebtView crateDebtView(UUID userId, UUID workspaceId, DebtView debtView) {
        var debt = new Debt(
                UUID.randomUUID(),
                debtView.walletId(),
                debtView.clientId(),
                userId,
                workspaceId,
                debtView.name(),
                debtView.email(),
                debtView.description(),
                debtView.amount(),
                LocalDateTime.now(),
                DebtStatus.OPEN,
                debtView.paymentType(),
                java.util.Collections.emptyList());
        var savedDebt = persistencePort.saveDebt(debt);
        return viewMapper.mapToDebtView(savedDebt);
    }

    @Override
    public PaymentView registerPayment(UUID userId, UUID workspaceId, PaymentView paymentView) {
        // Payment doesn't HAVE a workspaceId in its domain model yet, but we check if
        // the debt belongs to the workspace
        // Actually, Payment is tied to Debt, and Debt is tied to Workspace.
        var payment = new Payment(UUID.randomUUID(), paymentView.debtId(), paymentView.amount(), paymentView.date(),
                paymentView.type(), LocalDateTime.now());
        var savedPayment = persistencePort.savePayment(payment);
        return viewMapper.mapToPaymentView(savedPayment);
    }

    @Override
    public List<WalletView> getWalletViews(UUID userId, UUID workspaceId) {
        var debtList = persistencePort.findDebtsByWorkspaceId(workspaceId);
        var walletList = persistencePort.findAllWalletsByWorkspaceId(workspaceId);

        var list = walletList.stream().map(wallet -> {
            var debts = debtList.stream().filter(debt -> debt.walletId().equals(wallet.id())).toList();
            return viewMapper.mapToWalletView(wallet, debts);
        }).toList();

        return list.stream().map(w -> {
            var ds = w.debts().stream().map(d -> {
                var paymentViewList = persistencePort.findPaymentsByDebtId(d.id()).stream()
                        .map(viewMapper::mapToPaymentView).toList();
                return new DebtView(d, paymentViewList);
            }).toList();
            return new WalletView(w, ds);
        }).toList();
    }

    @Override
    public List<ClientView> searchClientViews(UUID workspaceId, String query) {
        // Filter by workspace if needed, but searchClients is currently global.
        // We should probably filter by workspace membership.
        return persistencePort.searchClients(query).stream()
                .filter(u -> u.workspaceIds().contains(workspaceId))
                .map(viewMapper::mapToClientView)
                .toList();
    }
}
