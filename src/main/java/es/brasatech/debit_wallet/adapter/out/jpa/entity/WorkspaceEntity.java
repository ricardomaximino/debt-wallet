package es.brasatech.debit_wallet.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "workspace")
public class WorkspaceEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToMany(mappedBy = "workspaces")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserEntity> users;
}
