package es.brasatech.debit_wallet.adapter.out.jpa.entity;

import es.brasatech.debit_wallet.domain.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private PlanRole planRole;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
