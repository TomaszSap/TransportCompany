package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.CollectionEnum;
import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;

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
    public Invoice deleteInvoice(Invoice invoice) {
      // invoiceMongoDao.removeInvoice();
        return null;
    }

    @Override
    public Invoice findById(int invoiceId) {
        return null;
      //  invoiceMongoDao.findInvoiceById();
    }

    @Override
    public boolean updateById(int invoiceId) {
        return false;
       // invoiceMongoDao.updateInvoiceById();
    }

    @Override
    public List<Invoice> getAll(Invoice invoice) {
        return null;
    }

    @Override
    public List<Invoice> findByClient(Invoice invoice) {
        return null;
       // invoiceMongoDao.findInvoiceByClient();
    }
}
