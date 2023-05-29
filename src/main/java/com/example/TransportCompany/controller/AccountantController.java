package com.example.TransportCompany.controller;

import com.example.TransportCompany.dto.ClientDTO;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Invoice;
import com.example.TransportCompany.model.Response;
import com.example.TransportCompany.services.ClientService;
import com.example.TransportCompany.services.InvoiceService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Optional;

@RestController
@RequestMapping("/accountant")
public class AccountantController {
    private static final Logger logger= LoggerFactory.getLogger(AccountantController.class);
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ClientService clientService;
    @PostMapping(value = "/createInvoice")
    public ResponseEntity<Response> createInvoice(@Valid @RequestBody Invoice invoice,@RequestParam int courseId)
    {
       Optional<Invoice> iSaved = Optional.ofNullable(invoiceService.addInvoice(invoice,courseId));
       Response response=new Response();
        if(iSaved.isPresent()){
            response.setStatusCode("200");
            response.setStatusMsg("Invoice saved successfully");
           return ResponseEntity.status(HttpStatus.CREATED).header("isInvSaved","true").body(response);}
       else {
           response.setStatusCode("400");
           response.setStatusCode("Invalid values!");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping(value = "/addClient")
    public ResponseEntity<Response> addClient(@Valid @RequestBody ClientDTO clientDTO, Errors errors)
    {
        Client client=modelMapper.map(clientDTO,Client.class);
        if (errors.hasErrors()) {
            logger.error("Client from validation failed due to: "+ errors);
            throw new ValidationException(String.valueOf(errors));
        }
        boolean iSaved = clientService.addClient(client);
        Response response=new Response();
        if(iSaved){
            response.setStatusCode("200");
            response.setStatusMsg("Client saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).header("isClientSaved","true").body(response);}
        else {
            response.setStatusCode("400");
            response.setStatusCode("Error while saving client !");
            return ResponseEntity.badRequest().header("isClientSaved","false").body(response);
        }
    }
    @GetMapping(value = "/getAllClients")
    public ResponseEntity<Object> getAllClients()
    {
        return ResponseEntity.ok().body(clientService.getAllClients());

    }
    @GetMapping(value = "/findInvoicesByClient")
    public ResponseEntity<Object> findInvoicesByClient(@RequestParam int clientId)
    {
        return ResponseEntity.ok().body(invoiceService.findByClient(clientId));

    }
    @DeleteMapping("/deleteClient")
    public ResponseEntity<String> deleteClient(@RequestParam int clientId)
    {
        boolean isDeleted=clientService.deleteClient(clientId);
        if (isDeleted) {
            return ResponseEntity.ok("Client has been deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The client with the specified ID does not exist.");
        }
    }
    @PostMapping(value = "/updateClient")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> updateClient(@RequestParam int clientId,@RequestBody ClientDTO clientDTO)
    {
        Client client=modelMapper.map(clientDTO,Client.class);

        Optional<Object> iSaved = Optional.ofNullable(clientService.updateClient(clientId,client));

        return iSaved.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body("Failed to update Client!"));
    }
    @PostMapping(value = "/updateInvoice")
    public ResponseEntity<Response> updateInvoice(@RequestParam int id,@RequestBody Invoice invoice)
    {
        logger.debug("called POST on endpoint /accountant/update");
        boolean isUpdated = invoiceService.updateById(String.valueOf(id),invoice);
        Response response=new Response();
        if(isUpdated)
        {
            response.setStatusCode("200");
            response.setStatusMsg("Invoice updated successfully");
            return ResponseEntity.status(HttpStatus.CREATED).header("isInvUpdated","true").body(response);
        }
        else
        {
            response.setStatusCode("400");
            response.setStatusCode("Invalid values!");
            return ResponseEntity.badRequest().header("isInvUpdated","false").body(response);
        }
    }
    @DeleteMapping("/deleteById")
    public ResponseEntity<Response> deleteInvoice(int id)
    {
        logger.debug("called DELETE on endpoint /accountant/update");
        boolean isDeleted =invoiceService.deleteInvoice(id);
        Response response=new Response();

        if(isDeleted)
        {
            response.setStatusCode("200");
            response.setStatusMsg("Invoice saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).header("isInvDeleted","true").body(response);
        }
        else
        {
            response.setStatusCode("400");
            response.setStatusCode("Invalid values!");
            return ResponseEntity.badRequest().header("isInvDeleted","false").body(response);
        }
    }
}
