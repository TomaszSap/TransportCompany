package com.example.TransportCompany.services;


import com.example.TransportCompany.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
     Invoice addInvoice(Invoice invoice,int clientId);
     boolean deleteInvoice(int id);
      Invoice findById(String invoiceId);
     boolean updateById(String invoiceId,Invoice invoice);
     List<Invoice> getAll(Invoice invoice);
     Invoice print(String invoice);

     List<Invoice> findByClient(String clientId);

}
