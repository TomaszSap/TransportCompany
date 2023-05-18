package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


@Slf4j
public class ClientServiceImpl implements ClientService{
   @Autowired
    ClientRepository clientRepository;
    @Autowired
    InvoiceMongoDao invoiceMongoDao;
    @Override
    public boolean addClient(Client client) {
        boolean isSaved=false;
        Client result=clientRepository.save(client);
        if(result!=null && result.getClientId()>0)
            isSaved=true;
        return isSaved;

    }
    @Override
    public boolean deleteClient(int clientId) {
        boolean isDeleted=false;
        Optional<Client> clientEntity=clientRepository.findById(clientId);
         if(clientEntity.get()!=null)
        {
       clientRepository.delete(clientEntity.get());
         isDeleted=true;
        }
        return isDeleted;
    }

    @Override
    public Object updateClient(String clientId, Client client) {
        boolean isUpdated=false;
        Optional<Client> clientEntity=clientRepository.findById(Integer.valueOf(clientId));
        Client updatedCourse=new Client();

        if (!clientEntity.isEmpty())
        {
            updatedCourse=clientRepository.save(clientEntity.get());
        }
        if(updatedCourse!=null && updatedCourse.getUpdatedBy()!=null)
            isUpdated=true;
        return isUpdated;
    }

    @Override
    public Client getClient(String clientId) {
        try {
            Optional<Client> clientEntity=clientRepository.findById(Integer.valueOf(clientId));
            if(!clientEntity.isEmpty())
            {
                return clientEntity.get();
            }
        }
        catch (Exception e)
        {
            log.error("error during getClient: "+ e);
        }
          return null;
    }
}
