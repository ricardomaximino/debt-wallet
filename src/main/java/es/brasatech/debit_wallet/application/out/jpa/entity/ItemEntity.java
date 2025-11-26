package es.brasatech.debit_wallet.application.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id @GeneratedValue private UUID id;
    private String name;
    @Column(columnDefinition = "text") private String description;
    private LocalDate date;
    private String category;
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}
