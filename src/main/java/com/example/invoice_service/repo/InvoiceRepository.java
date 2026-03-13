package com.example.invoice_service.repo;

import com.example.invoice_service.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByPoId(Long poId);
    List<Invoice> findByPoNumber(String poNumber);
    List<Invoice> findByStatus(String status);
}
