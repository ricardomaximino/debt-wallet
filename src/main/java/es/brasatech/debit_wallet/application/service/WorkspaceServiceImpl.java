package es.brasatech.debit_wallet.application.service;

import es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.PlanRole;
import es.brasatech.debit_wallet.domain.model.User;
import es.brasatech.debit_wallet.domain.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceUseCase {

    private final WalletPersistencePort persistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Workspace createWorkspace(UUID ownerId, String name) {
        String slug = name.toLowerCase().replaceAll("[^a-z0-9]", "-");
        Workspace workspace = persistencePort.saveWorkspace(new Workspace(UUID.randomUUID(), name, slug, ownerId));

        // Link owner to workspace
        persistencePort.findUserById(ownerId).ifPresent(user -> {
            user.workspaceIds().add(workspace.id());
            persistencePort.saveUser(user);
        });

        return workspace;
    }

    @Override
    public List<Workspace> getWorkspaces(UUID userId) {
        return persistencePort.findWorkspacesByUserId(userId);
    }

    @Override
    public java.util.Optional<Workspace> getWorkspaceBySlug(String slug) {
        return persistencePort.findWorkspaceBySlug(slug);
    }

    @Override
    @Transactional
    public void deleteUserWorkspaces(UUID ownerId) {
        List<Workspace> workspaces = persistencePort.findWorkspacesByUserId(ownerId).stream()
                .filter(w -> ownerId.equals(w.ownerId()))
                .toList();

        for (Workspace workspace : workspaces) {
            deleteWorkspace(workspace.id());
        }
    }

    @Override
    @Transactional
    public void deleteWorkspace(UUID workspaceId) {
        persistencePort.removeWorkspaceFromUsers(workspaceId);
        persistencePort.deleteWorkspace(workspaceId);
    }

    @Override
    public List<User> getUsersInWorkspace(UUID workspaceId) {
        return persistencePort.findUsersByWorkspaceId(workspaceId);
    }

    @Override
    @Transactional
    public User addUserToWorkspace(UUID workspaceId, String name, String email, String username, String password,
            es.brasatech.debit_wallet.domain.model.UserRole role) {
        User newUser = new User(
                UUID.randomUUID(),
                name,
                null,
                null,
                null,
                null,
                email,
                username,
                passwordEncoder.encode(password),
                null,
                true,
                PlanRole.FREE,
                Set.of(role),
                Set.of(workspaceId),
                LocalDateTime.now());
        return persistencePort.saveUser(newUser);
    }
}
