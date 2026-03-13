package com.example.invoice_service.service;

import com.example.invoice_service.dto.InvoiceResponse;
import com.example.invoice_service.model.Invoice;
import com.example.invoice_service.repo.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return mapToResponse(invoice);
    }

    public List<InvoiceResponse> getInvoicesByPoId(Long poId) {
        return invoiceRepository.findByPoId(poId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InvoiceResponse> getInvoicesByPoNumber(String poNumber) {
        return invoiceRepository.findByPoNumber(poNumber).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvoiceResponse updateInvoiceStatus(Long id, String status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        invoice.setStatus(status);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice status updated: {} -> {}", id, status);

        return mapToResponse(updatedInvoice);
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setPoId(invoice.getPoId());
        response.setPoNumber(invoice.getPoNumber());
        response.setCustomerName(invoice.getCustomerName());
        response.setAmount(invoice.getAmount());
        response.setDescription(invoice.getDescription());
        response.setStatus(invoice.getStatus());
        response.setInvoiceDate(invoice.getInvoiceDate().format(DateTimeFormatter.ISO_DATE_TIME));
        response.setDueDate(invoice.getDueDate().format(DateTimeFormatter.ISO_DATE_TIME));
        return response;
    }
}
