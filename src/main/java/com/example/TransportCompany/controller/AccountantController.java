package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Invoice;
import com.example.TransportCompany.model.Response;
import com.example.TransportCompany.services.ClientService;
import com.example.TransportCompany.services.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
//@RequestMapping("accountant")
public class AccountantController  extends RestEndpoint{
    private static final Logger logger= LoggerFactory.getLogger(AccountantController.class);
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ClientService clientService;

/*    @Autowired
    EmployeeService employeeService;
    @PatchMapping("/user")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody Employee employee)
    {  return super.updateUser(id,employee);
    }
*/
    @PostMapping(value = "/createInvoice")
    public ResponseEntity<Response> createInvoice(@Valid @RequestBody Invoice invoice)
    {
       Optional<Invoice> iSaved = Optional.ofNullable(invoiceService.addInvoice(invoice));
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
    public ResponseEntity<Response> addClient(@RequestBody Client client)
    {
        Optional<Boolean> iSaved = Optional.ofNullable(clientService.addClient(client));
        Response response=new Response();
        if(iSaved.isPresent()){
            response.setStatusCode("200");
            response.setStatusMsg("Invoice saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).header("isClientSaved","true").body(response);}
        else {
            response.setStatusCode("400");
            response.setStatusCode("Invalid values!");
            return ResponseEntity.badRequest().header("isClientSaved","false").body(response);
        }
    }
    @PostMapping(value = "/updateClient")
    public ResponseEntity<Object> updateClient(@RequestParam String clientId, @RequestBody Client client)
    {
        Optional<Object> iSaved = Optional.ofNullable(clientService.updateClient(clientId,client));

        if(iSaved.isPresent())
            return ResponseEntity.ok(iSaved.get());
        else
            return ResponseEntity.badRequest().body(iSaved.get());
    }
    @PostMapping(value = "/updateInvoice")
    public ResponseEntity<Response> updateInvoice(@RequestParam int id,@RequestBody Invoice invoice)
    {
        logger.debug("called POST on endpoint /accountant/update");
        Optional<Boolean> isUpdated = Optional.ofNullable(invoiceService.updateById(String.valueOf(id),invoice));
        Response response=new Response();
        if(isUpdated.isPresent())
        {
            response.setStatusCode("200");
            response.setStatusMsg("Invoice saved successfully");
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

        if(isDeleted==true)
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
