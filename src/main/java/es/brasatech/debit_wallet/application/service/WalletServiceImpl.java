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

    private UUID getDefaultWorkspaceId(UUID userId) {
        return persistencePort.findUserById(userId)
                .flatMap(user -> user.workspaceIds().stream().findFirst())
                .orElseThrow(() -> new RuntimeException("User not found or has no workspaces: " + userId));
    }

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
    public WalletView createWalletView(UUID userId, String name) {
        var workspaceId = getDefaultWorkspaceId(userId);
        var wallet = new Wallet(UUID.randomUUID(), name, LocalDateTime.now(), userId, workspaceId);
        var savedWallet = persistencePort.saveWallet(wallet);
        return viewMapper.mapToWalletView(savedWallet);
    }

    @Override
    public ClientView createClientView(String name, String email) {
        var workspaceId = getDefaultWorkspaceId(getLoggedUseId());
        String username = usernameGenerator.generate(name);

        // A client is a User with role CLIENT
        var client = new User(
                UUID.randomUUID(),
                name,
                email,
                username,
                "{noop}client123", // Default password for new clients
                true,
                PlanRole.FREE,
                java.util.Set.of(UserRole.CLIENT),
                java.util.Set.of(workspaceId));

        var savedClient = persistencePort.saveUser(client);
        return viewMapper.mapToClientView(savedClient);
    }

    @Override
    public DebtView crateDebtView(UUID userId, DebtView debtView) {
        var workspaceId = getDefaultWorkspaceId(userId);
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
    public PaymentView registerPayment(UUID userId, PaymentView paymentView) {
        var payment = new Payment(UUID.randomUUID(), paymentView.debtId(), paymentView.amount(), paymentView.date(),
                paymentView.type(), LocalDateTime.now());
        var savedPayment = persistencePort.savePayment(payment);
        return viewMapper.mapToPaymentView(savedPayment);
    }

    @Override
    public List<WalletView> getWalletViews(UUID userId) {
        var debtList = persistencePort.findDebtsByUserId(userId);
        var walletList = persistencePort.findAllWalletsByUserId(userId);

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
    public List<ClientView> searchClientViews(String query) {
        return persistencePort.searchClients(query).stream()
                .map(viewMapper::mapToClientView)
                .toList();
    }
}
