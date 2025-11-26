package es.brasatech.debit_wallet.application.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "portfolio")
public class PortfolioEntity {
    @Id @GeneratedValue private UUID id;
    private String name;
    @Column(columnDefinition = "text") private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}
