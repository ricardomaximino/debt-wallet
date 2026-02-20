package es.brasatech.debit_wallet.adapter.out.jpa.mapper;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.*;
import es.brasatech.debit_wallet.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    @Mapping(target = "workspace.id", source = "workspaceId")
    DebtEntity mapToDebtEntity(Debt debt);

    @Mapping(target = "workspaceId", source = "workspace.id")
    Debt mapToDebt(DebtEntity debtEntity);

    ItemEntity mapToItemEntity(Item item);

    Item mapToItem(ItemEntity itemEntity);

    PaymentEntity mapToPaymentEntity(Payment payment);

    Payment mapToPayment(PaymentEntity paymentEntity);

    @Mapping(target = "workspace.id", source = "workspaceId")
    WalletEntity mapToWalletEntity(Wallet wallet);

    @Mapping(target = "workspaceId", source = "workspace.id")
    Wallet mapToWallet(WalletEntity walletEntity);

    @Mapping(target = "workspaces", source = "workspaceIds")
    UserEntity mapToUserEntity(User user);

    @Mapping(target = "workspaceIds", source = "workspaces")
    User mapToUser(UserEntity userEntity);

    default WorkspaceEntity mapIdToWorkspace(UUID id) {
        if (id == null)
            return null;
        WorkspaceEntity entity = new WorkspaceEntity();
        entity.setId(id);
        return entity;
    }

    default UUID mapWorkspaceToId(WorkspaceEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    default Set<WorkspaceEntity> mapIdsToWorkspaces(Set<UUID> ids) {
        if (ids == null)
            return null;
        return ids.stream().map(this::mapIdToWorkspace).collect(Collectors.toSet());
    }

    default Set<UUID> mapWorkspacesToIds(Set<WorkspaceEntity> entities) {
        if (entities == null)
            return null;
        return entities.stream().map(this::mapWorkspaceToId).collect(Collectors.toSet());
    }

    WorkspaceEntity mapToWorkspaceEntity(Workspace workspace);

    Workspace mapToWorkspace(WorkspaceEntity workspaceEntity);
}
