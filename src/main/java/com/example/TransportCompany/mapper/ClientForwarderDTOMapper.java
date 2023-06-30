package com.example.TransportCompany.mapper;


import com.example.TransportCompany.dto.ClientForwarderDTO;
import com.example.TransportCompany.model.Client;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClientForwarderDTOMapper implements Function<Client, ClientForwarderDTO> {
    @Override
    public ClientForwarderDTO apply(Client client) {
        return new ClientForwarderDTO(
                client.getEmail(),
                client.getCompany()
        );
    }
}
