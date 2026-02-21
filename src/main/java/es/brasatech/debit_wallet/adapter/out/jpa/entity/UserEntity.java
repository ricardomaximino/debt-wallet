package es.brasatech.debit_wallet.adapter.out.jpa.entity;

import es.brasatech.debit_wallet.domain.model.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String firstName;
    private String middleName;
    private String surname;
    private java.time.LocalDate birthday;
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String profilePicturePath;

    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    private PlanRole planRole;

    @org.hibernate.annotations.CreationTimestamp
    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserRole> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_workspace", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "workspace_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<WorkspaceEntity> workspaces;
}
