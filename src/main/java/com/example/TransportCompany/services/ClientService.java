package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Client;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
      boolean addClient(Client client);
      boolean deleteClient(int clientId);
    Object updateClient(String clientId, Client client);

    Client getClient(String clientId);
}
