package com.example.invoice_service.dto;

import lombok.Data;

@Data
public class OrderEvent {
    private String eventType;
    private Long poId;
    private String poNumber;
    private String customerName;
    private Double amount;
    private String description;
}