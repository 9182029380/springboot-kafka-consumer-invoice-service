package com.example.invoice_service.kafka;

import com.example.invoice_service.dto.OrderEvent;
import com.example.invoice_service.model.Invoice;
import com.example.invoice_service.repo.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final InvoiceRepository invoiceRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "invoice-group",
            containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeOrderEvent(OrderEvent orderEvent) {
        log.info("Received order event: {}", orderEvent);

        try {
            if ("CREATED".equals(orderEvent.getEventType())) {
                createInvoiceFromOrder(orderEvent);
            } else if ("UPDATED".equals(orderEvent.getEventType())) {
                updateInvoiceFromOrder(orderEvent);
            }
        } catch (Exception e) {
            log.error("Error processing order event: {}", e.getMessage(), e);
        }
    }

    private void createInvoiceFromOrder(OrderEvent orderEvent) {
        // Check if invoice already exists for this PO
        if (!invoiceRepository.findByPoId(orderEvent.getPoId()).isEmpty()) {
            log.info("Invoice already exists for PO ID: {}", orderEvent.getPoId());
            return;
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setPoId(orderEvent.getPoId());
        invoice.setPoNumber(orderEvent.getPoNumber());
        invoice.setCustomerName(orderEvent.getCustomerName());
        invoice.setAmount(orderEvent.getAmount());
        invoice.setDescription(orderEvent.getDescription());
        invoice.setStatus("PENDING");

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice created from order: {}", savedInvoice.getInvoiceNumber());
    }

    private void updateInvoiceFromOrder(OrderEvent orderEvent) {
        List<Invoice> invoices = invoiceRepository.findByPoId(orderEvent.getPoId());
        if (!invoices.isEmpty()) {
            Invoice invoice = invoices.get(0); // Assuming one invoice per PO
            invoice.setAmount(orderEvent.getAmount());
            invoice.setDescription(orderEvent.getDescription());
            invoice.setCustomerName(orderEvent.getCustomerName());
            invoiceRepository.save(invoice);
            log.info("Invoice updated for PO ID: {}", orderEvent.getPoId());
        }
    }
}
