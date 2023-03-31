package com.example.TransportCompany.services;


import com.example.TransportCompany.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    abstract Invoice addInvoice(Invoice invoice);
    abstract Invoice findAndModifyInvoice(Invoice invoice);
    abstract Invoice deleteInvoice(Invoice invoice);
    abstract  Invoice findById(int invoiceId);
    abstract boolean updateById(int invoiceId);
    abstract List<Invoice> getAll(Invoice invoice);

    abstract List<Invoice> findByClient(Invoice invoice);

}
