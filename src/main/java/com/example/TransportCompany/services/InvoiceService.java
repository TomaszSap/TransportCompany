package com.example.TransportCompany.services;


import com.example.TransportCompany.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    abstract Invoice addInvoice(Invoice invoice);
    abstract Invoice findAndModifyInvoice(Invoice invoice);
    abstract boolean deleteInvoice(int id);
    abstract  Invoice findById(String invoiceId);
    abstract boolean updateById(String invoiceId,Invoice invoice);
    abstract List<Invoice> getAll(Invoice invoice);

    abstract Invoice print(String invoice);

    abstract List<Invoice> findByClient(Invoice invoice);

}
