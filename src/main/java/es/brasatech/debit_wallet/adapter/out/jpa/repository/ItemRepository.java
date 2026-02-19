package es.brasatech.debit_wallet.adapter.out.jpa.repository;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {
}


