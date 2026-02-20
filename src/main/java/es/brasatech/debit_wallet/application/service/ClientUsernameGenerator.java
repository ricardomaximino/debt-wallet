package es.brasatech.debit_wallet.application.service;

import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class ClientUsernameGenerator {

    private final WalletPersistencePort persistencePort;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random();

    public String generate(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            fullName = "client";
        }

        String[] parts = fullName.trim().split("\\s+");
        String first = parts[0].toLowerCase().replaceAll("[^a-z]", "");
        String last = parts.length > 1 ? parts[parts.length - 1].toLowerCase().replaceAll("[^a-z]", "") : "user";

        String base = first + "." + last;
        String username = base;

        while (persistencePort.existsByUsername(username)) {
            username = base + randomSuffix();
        }

        return username;
    }

    private String randomSuffix() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int character = random.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}