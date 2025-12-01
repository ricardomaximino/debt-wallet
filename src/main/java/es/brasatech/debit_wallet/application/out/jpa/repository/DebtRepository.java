package es.brasatech.debit_wallet.application.out.jpa.repository;

import es.brasatech.debit_wallet.application.out.jpa.entity.DebtEntity;
import es.brasatech.debit_wallet.domain.DebtStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DebtRepository extends JpaRepository<DebtEntity, UUID> {
    List<DebtEntity> findByWalletId(UUID walletId);
    List<DebtEntity> findByDebtorId(UUID debtorId);
    List<DebtEntity> findByStatus(DebtStatus status);
    List<DebtEntity> findByUserId(UUID useId);
}
