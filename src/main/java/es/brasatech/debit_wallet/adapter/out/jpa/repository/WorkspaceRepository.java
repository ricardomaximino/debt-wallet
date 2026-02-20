package es.brasatech.debit_wallet.adapter.out.jpa.repository;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {
    Optional<WorkspaceEntity> findBySlug(String slug);
}
