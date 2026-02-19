package es.brasatech.debit_wallet.adapter.out.jpa.repository;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    List<PaymentEntity> findByDebtId(UUID debtId);
}


