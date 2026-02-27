package es.brasatech.debit_wallet.application.port_in;

import es.brasatech.debit_wallet.domain.model.User;
import es.brasatech.debit_wallet.domain.model.Workspace;

import java.util.List;
import java.util.UUID;

public interface WorkspaceUseCase {
    Workspace createWorkspace(UUID ownerId, String name);

    List<Workspace> getWorkspaces(UUID userId);

    java.util.Optional<Workspace> getWorkspaceBySlug(String slug);

    void deleteUserWorkspaces(UUID ownerId);

    void deleteWorkspace(UUID workspaceId);

    List<User> getUsersInWorkspace(UUID workspaceId);

    User addUserToWorkspace(UUID workspaceId, String name, String email, String username, String password,
            es.brasatech.debit_wallet.domain.model.UserRole role);
}
