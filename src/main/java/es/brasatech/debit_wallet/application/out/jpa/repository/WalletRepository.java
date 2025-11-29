package es.brasatech.debit_wallet.application.out.jpa.repository;

import es.brasatech.debit_wallet.application.out.jpa.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    List<WalletEntity> findAllByUserId(UUID userId);
}
