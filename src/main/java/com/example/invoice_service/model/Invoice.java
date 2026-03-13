package com.example.invoice_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private Long poId;

    @Column(nullable = false)
    private String poNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(nullable = false)
    private String status;

    private LocalDateTime invoiceDate;

    private LocalDateTime dueDate;

    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        if (invoiceDate == null) {
            invoiceDate = LocalDateTime.now();
        }
        if (dueDate == null) {
            dueDate = LocalDateTime.now().plusDays(30);
        }
    }
}
