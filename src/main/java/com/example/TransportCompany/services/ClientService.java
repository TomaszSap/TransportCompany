package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Invoice;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Service
public interface ClientService {
     abstract boolean addClient(Client client);
     abstract boolean deleteClient(int clientId);
     abstract Invoice addInvoice(int clientId);
     abstract Invoice deleteInvoice(int clientId);

    void deleteInvoice(@RequestParam int invoiceId, HttpSession httpSession);

    Object updateClient(String clientId, Client client);

    Client getClient(String clientId);
}
