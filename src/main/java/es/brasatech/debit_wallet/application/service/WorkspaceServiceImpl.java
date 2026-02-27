package es.brasatech.debit_wallet.application.service;

import es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceUseCase {

    private final WalletPersistencePort persistencePort;

    @Override
    @Transactional
    public Workspace createWorkspace(UUID ownerId, String name) {
        String slug = name.toLowerCase().replaceAll("[^a-z0-9]", "-");
        Workspace workspace = new Workspace(UUID.randomUUID(), name, slug, ownerId);
        return persistencePort.saveWorkspace(workspace);
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
}
