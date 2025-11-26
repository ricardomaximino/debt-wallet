package es.brasatech.debit_wallet.application.out.jpa.repository;

import es.brasatech.debit_wallet.application.out.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    List<PaymentEntity> findByDebtId(UUID debtId);
}
