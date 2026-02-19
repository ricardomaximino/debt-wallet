package es.brasatech.debit_wallet.adapter.out.jpa.repository;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DebtRepository extends JpaRepository<DebtEntity, UUID> {
    List<DebtEntity> findByUserId(UUID userId);

    List<DebtEntity> findByWalletId(UUID walletId);
}


