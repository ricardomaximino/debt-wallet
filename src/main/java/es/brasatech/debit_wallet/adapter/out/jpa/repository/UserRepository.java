package es.brasatech.debit_wallet.adapter.out.jpa.repository;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @org.springframework.data.jpa.repository.Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r = 'CLIENT' AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<UserEntity> searchClients(String query);
}
