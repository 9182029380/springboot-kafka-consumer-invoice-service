package com.example.invoice_service.controller;

import com.example.invoice_service.dto.InvoiceResponse;
import com.example.invoice_service.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/po/{poId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPoId(@PathVariable Long poId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByPoId(poId));
    }

    @GetMapping("/ponumber/{poNumber}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPoNumber(@PathVariable String poNumber) {
        return ResponseEntity.ok(invoiceService.getInvoicesByPoNumber(poNumber));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(invoiceService.updateInvoiceStatus(id, status));
    }
}
