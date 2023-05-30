package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.TransportCompany.constant.AppConstants.getNullPropertyNames;


@Slf4j
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    InvoiceMongoDao invoiceMongoDao;

    @Override
    public boolean addClient(Client client) {
        boolean isSaved = false;
        Optional<Client> isAccount = Optional.ofNullable(clientRepository.findByEmail(client.getEmail()));
        if (isAccount.isEmpty()) {

            Client result = clientRepository.save(client);
            if (result != null && result.getClientId() > 0)
                isSaved = true;
        }
        return isSaved;

    }

    @Override
    public boolean deleteClient(int clientId) {
        boolean isDeleted = false;
        Optional<Client> clientEntity = clientRepository.findById(clientId);
        if (clientEntity.get() != null) {
            clientRepository.delete(clientEntity.get());
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Object updateClient(int clientId, Client client) {
        boolean isUpdated = false;
        Optional<Client> clientEntity = clientRepository.findById(Integer.valueOf(clientId));
        Client updatedClient = new Client();
        client.setUpdatedBy("ADMIN");
        if (!clientEntity.isEmpty()) {
            client.setClientId(clientEntity.get().getClientId());
            if (client.getEmail() != null && client.getEmail() == clientEntity.get().getEmail() || client.getEmail() == null)
                clientEntity.get().setConfirmEmail(clientEntity.get().getEmail());
            BeanUtils.copyProperties(client, clientEntity.get(), getNullPropertyNames(client));
            updatedClient = clientRepository.save(clientEntity.get());
        }
        if (updatedClient != null && updatedClient.getUpdatedBy() != null)
            isUpdated = true;
        return isUpdated;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClient(int clientId) {
        return clientRepository.findById(clientId);
    }
}
