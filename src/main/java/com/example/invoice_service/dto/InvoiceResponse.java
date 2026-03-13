package com.example.invoice_service.dto;

import lombok.Data;

@Data
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private Long poId;
    private String poNumber;
    private String customerName;
    private Double amount;
    private String description;
    private String status;
    private String invoiceDate;
    private String dueDate;
}
