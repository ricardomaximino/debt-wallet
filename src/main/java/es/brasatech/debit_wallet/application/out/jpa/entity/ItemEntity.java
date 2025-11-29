package es.brasatech.debit_wallet.application.out.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    @Column(columnDefinition = "text") private String description;
    private LocalDate date;
    private String category;
    private LocalDateTime createdAt = LocalDateTime.now();
    private UUID itemId;
}
