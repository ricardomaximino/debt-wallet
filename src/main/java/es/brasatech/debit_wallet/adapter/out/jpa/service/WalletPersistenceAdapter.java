package es.brasatech.debit_wallet.adapter.out.jpa.service;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.WorkspaceEntity;
import es.brasatech.debit_wallet.adapter.out.jpa.mapper.WalletMapper;
import es.brasatech.debit_wallet.adapter.out.jpa.repository.*;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements WalletPersistencePort {

    private final WalletRepository walletRepository;
    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WalletMapper mapper;

    @Override
    public Wallet saveWallet(Wallet wallet) {
        var entity = mapper.mapToWalletEntity(wallet);
        return mapper.mapToWallet(walletRepository.save(entity));
    }

    @Override
    public List<Wallet> findAllWalletsByUserId(UUID userId) {
        return walletRepository.findAllByUserId(userId).stream()
                .map(mapper::mapToWallet)
                .toList();
    }

    @Override
    public List<Wallet> findAllWalletsByWorkspaceId(UUID workspaceId) {
        return walletRepository.findAll().stream()
                .filter(w -> workspaceId.equals(w.getWorkspace().getId()))
                .map(mapper::mapToWallet)
                .toList();
    }

    @Override
    public Debt saveDebt(Debt debt) {
        var entity = mapper.mapToDebtEntity(debt);
        return mapper.mapToDebt(debtRepository.save(entity));
    }

    @Override
    public List<Debt> findDebtsByUserId(UUID userId) {
        return debtRepository.findByUserId(userId).stream()
                .map(mapper::mapToDebt)
                .toList();
    }

    @Override
    public List<Debt> findDebtsByWalletId(UUID walletId) {
        return debtRepository.findByWalletId(walletId).stream()
                .map(mapper::mapToDebt)
                .toList();
    }

    @Override
    public List<Debt> findDebtsByWorkspaceId(UUID workspaceId) {
        return debtRepository.findAll().stream()
                .filter(d -> workspaceId.equals(d.getWorkspace().getId()))
                .map(mapper::mapToDebt)
                .toList();
    }

    @Override
    public Payment savePayment(Payment payment) {
        var entity = mapper.mapToPaymentEntity(payment);
        return mapper.mapToPayment(paymentRepository.save(entity));
    }

    @Override
    public List<Payment> findPaymentsByDebtId(UUID debtId) {
        return paymentRepository.findByDebtId(debtId).stream()
                .map(mapper::mapToPayment)
                .toList();
    }

    @Override
    public List<User> searchClients(String query) {
        return userRepository.searchClients(query).stream()
                .map(mapper::mapToUser)
                .toList();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(mapper::mapToUser);
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return userRepository.findById(userId).map(mapper::mapToUser);
    }

    @Override
    public Optional<Workspace> findWorkspaceBySlug(String slug) {
        return workspaceRepository.findBySlug(slug).map(mapper::mapToWorkspace);
    }

    @Override
    public User saveUser(User user) {
        return mapper.mapToUser(userRepository.save(mapper.mapToUserEntity(user)));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Workspace saveWorkspace(Workspace workspace) {
        return mapper.mapToWorkspace(workspaceRepository.save(mapper.mapToWorkspaceEntity(workspace)));
    }

    @Override
    public void deleteWorkspace(UUID workspaceId) {
        workspaceRepository.deleteById(workspaceId);
    }

    @Override
    public List<Workspace> findWorkspacesByUserId(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> user.getWorkspaces().stream()
                        .map(mapper::mapToWorkspace)
                        .toList())
                .orElse(List.of());
    }

    @Override
    public void removeWorkspaceFromUsers(UUID workspaceId) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found: " + workspaceId));

        userRepository.findAll().stream()
                .filter(user -> user.getWorkspaces().contains(workspace))
                .forEach(user -> {
                    user.getWorkspaces().remove(workspace);
                    userRepository.save(user);
                });
    }

    @Override
    public List<User> findUsersByWorkspaceId(UUID workspaceId) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found: " + workspaceId));
        return userRepository.findAll().stream()
                .filter(user -> user.getWorkspaces().contains(workspace))
                .map(mapper::mapToUser)
                .toList();
    }
}
