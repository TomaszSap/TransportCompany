package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {
      boolean addClient(Client client);
      boolean deleteClient(int clientId);
    Object updateClient(int clientId, Client client);
    List<Client> getAllClients();

    Client getClient(int clientId);
}
