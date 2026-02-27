package es.brasatech.debit_wallet.adapter.in.web.config;

import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.PlanRole;
import es.brasatech.debit_wallet.domain.model.User;
import es.brasatech.debit_wallet.domain.model.UserRole;
import es.brasatech.debit_wallet.domain.model.Workspace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityDataInitializer implements CommandLineRunner {

        private final WalletPersistencePort persistencePort;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
                log.info("Initializing security data...");

                // Create Workspaces
                Workspace officeA = createWorkspace(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                                "Maximino & Associados", "maximino-associados");
                Workspace officeB = createWorkspace(UUID.fromString("22222222-2222-2222-2222-222222222222"),
                                "Global Law Partners", "global-law");

                // Create Users
                // Dr. Ricardo belongs to both workspaces
                createUser(UUID.fromString("fd4437c0-bda6-489d-964f-7e43169cace0"), "ricardo", "Dr. Ricardo Maximino",
                                "ricardo@mail.com", "password",
                                Set.of(UserRole.LAWYER), Set.of(officeA.id(), officeB.id()));

                // Admin user
                createUser(UUID.randomUUID(), "admin", "System Admin", "admin@mail.com", "admin123",
                                Set.of(UserRole.ADMIN), Set.of());

                // Client user
                createUser(UUID.randomUUID(), "client", "Roberto Alencar", "roberto@mail.com", "client123",
                                Set.of(UserRole.CLIENT), Set.of(officeA.id()));

                log.info("Security data initialized successfully.");
        }

        private Workspace createWorkspace(UUID id, String name, String slug) {
                return persistencePort.findWorkspaceBySlug(slug)
                                .orElseGet(() -> persistencePort.saveWorkspace(new Workspace(id, name, slug, null)));
        }

        private void createUser(UUID id, String username, String name, String email, String password,
                        Set<UserRole> roles, Set<UUID> workspaceIds) {
                if (persistencePort.findUserByUsername(username).isEmpty()) {
                        User user = new User(
                                        id,
                                        name,
                                        null, // firstName
                                        null, // middleName
                                        null, // surname
                                        null, // birthday
                                        email,
                                        username,
                                        passwordEncoder.encode(password),
                                        null, // profilePicturePath
                                        true,
                                        PlanRole.FREE,
                                        roles,
                                        workspaceIds,
                                        java.time.LocalDateTime.now() // createdAt
                        );
                        persistencePort.saveUser(user);
                        log.info("Created user: {} with roles: {}", username, roles);
                }
        }
}
