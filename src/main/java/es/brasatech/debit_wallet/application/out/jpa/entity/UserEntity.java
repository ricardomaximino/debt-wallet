package es.brasatech.debit_wallet.application.out.jpa.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEntity {
    private UUID id;
    private String name;
    private String email;
}
