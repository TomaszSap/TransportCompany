package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Invoice;
import com.example.TransportCompany.model.PrintInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


//todo
public class InvoiceServiceImpl implements  InvoiceService{
    @Autowired
    CourseService courseService;
    @Autowired
    ClientService clientService;
    @Autowired
    InvoiceMongoDao invoiceMongoDao;
    @Autowired
    @Override
    public Invoice addInvoice(Invoice invoice) {
        return invoiceMongoDao.saveToMongo(invoice);
    }

    @Override
    public Invoice findAndModifyInvoice(Invoice invoice) {
        return null;
    }
    @Override
    public boolean deleteInvoice(int id) {
        Query query=new Query();
        query.addCriteria(Criteria.where("objectId").is(id));
        return invoiceMongoDao.removeInvoice(query);
    }

    @Override
    public Invoice findById(String invoiceId) {
      return invoiceMongoDao.findInvoiceById(invoiceId);
    }

    @Override
    public boolean updateById(String invoiceId,Invoice invoice) {
        return false;
       // invoiceMongoDao.updateInvoiceById();
    }

    @Override
    public List<Invoice> getAll(Invoice invoice) {
        return null;
    }

    @Override
    public Invoice print(String invoiceId) {
        Invoice invoice=findById(invoiceId);
        Client client=clientService.getClient(invoice.getClientId());
        PrintInvoice printInvoice= (PrintInvoice) invoice;
        printInvoice.setClient(client);
        return printInvoice;
    }

    @Override
    public List<Invoice> findByClient(Invoice invoice) {
        return null;
       // invoiceMongoDao.findInvoiceByClient();
    }
}
