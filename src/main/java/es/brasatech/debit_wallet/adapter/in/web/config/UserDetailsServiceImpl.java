package es.brasatech.debit_wallet.adapter.in.web.config;

import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

        private final WalletPersistencePort persistencePort;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("Attempting to load user: {}", username);
                return persistencePort.findUserByUsername(username)
                                .map(user -> {
                                        log.info("User found: {}, roles: {}", username, user.roles());
                                        return org.springframework.security.core.userdetails.User.builder()
                                                        .username(user.username())
                                                        .password(user.password())
                                                        .disabled(!user.enabled())
                                                        .authorities(user.roles().stream()
                                                                        .map(role -> new SimpleGrantedAuthority(
                                                                                        "ROLE_" + role.name()))
                                                                        .collect(Collectors.toSet()))
                                                        .build();
                                })
                                .orElseGet(() -> {
                                        log.error("User not found: {}", username);
                                        throw new UsernameNotFoundException("User not found: " + username);
                                });
        }
}
