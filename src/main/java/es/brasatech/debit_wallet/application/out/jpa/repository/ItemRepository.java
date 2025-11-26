package es.brasatech.debit_wallet.application.out.jpa.repository;

import es.brasatech.debit_wallet.application.out.jpa.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {
}
