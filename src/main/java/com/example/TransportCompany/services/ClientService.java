package com.example.TransportCompany.services;

import com.example.TransportCompany.dto.ClientForwarderDTO;
import com.example.TransportCompany.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ClientService {
    boolean addClient(Client client);

    boolean deleteClient(int clientId);

    Object updateClient(int clientId, Client client);

    List<ClientForwarderDTO> getAllClients();

    Optional<Client> getClient(int clientId);
}
