package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Invoice;
import com.example.TransportCompany.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

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
    public Invoice addInvoice(int clientId) {
        return null;
    }

    @Override
    public Invoice deleteInvoice(int clientId) {
        return null;
    }

    public void addInvoice(@RequestParam("invoice") Invoice invoice, HttpSession httpSession) {
        Client client = (Client) httpSession.getAttribute("client"); //getting client from session
        Invoice invoiceDocument = invoiceMongoDao.findInvoiceById(String.valueOf(invoice.getObjectId()),Invoice.class); //try to find person with given email
        if (invoiceDocument == null || !(invoiceDocument.getObjectId() !=null)||invoiceDocument.getClientId()!=null) //validation if is possible
        {

            //todo
            //modelAndView.setViewName("redirect:/admin/displayStudents?classId="+ schoolClass.getClassId()+"&error=true");
           // return modelAndView;
        }
        Update update=new Update();
        update.set("clientId",client.getClientId());
        invoiceMongoDao.updateInvoiceById(String.valueOf(invoiceDocument.getObjectId()),update,Invoice.class);
        //
    }

    @Override
    public void deleteInvoice(@RequestParam int invoiceId, HttpSession httpSession) {
        Client client = (Client) httpSession.getAttribute("client"); //getting schoolClass from session
        Invoice invoiceDocument = invoiceMongoDao.findInvoiceById(String.valueOf(invoiceId),Invoice.class); //try to find person with given email
        invoiceDocument.setClientId(null);
        invoiceMongoDao.saveToMongo(invoiceDocument);
        httpSession.setAttribute("client",client);
       // modelAndView.setViewName("redirect:/admin/displayStudents?classId="+ schoolClass.getClassId());
        //return modelAndView;
    }
}
