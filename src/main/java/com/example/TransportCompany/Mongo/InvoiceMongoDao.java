package com.example.TransportCompany.Mongo;

import com.example.TransportCompany.model.Invoice;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class InvoiceMongoDao extends AbstractMongoDao{
    private static final CollectionEnum collection=CollectionEnum.INVOICES;
    private static Invoice invoice=new Invoice();
    @Override
    public MongoTemplate getMongoTemplate() {
        return null;
    }

    @Override
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
    }

    public Invoice saveToMongo(Invoice object)
    {
        return (Invoice)  save(object,collection);

    }
    public Invoice findInvoiceById(String invoiceId){

        Query query=new Query();
        query.addCriteria(Criteria.where("objectId").is(invoiceId));
         return (Invoice) findById(invoiceId,invoice.getClass(),collection);
    }
    public Invoice assignInvoice(String invoiceId,Update update){
        Query query=new Query();
        query.addCriteria(Criteria.where("objectId").is(invoiceId));

        return (Invoice) findAndModify(query,invoice.getClass(),update,collection);
    }
    public void findInvoiceAndModifyById(String invoiceId,Invoice invoice) {
        findInvoiceAndModifyById(invoiceId,invoice,collection);
    }
    private void findInvoiceAndModifyById(String invoiceId,Invoice invoice, CollectionEnum collection) {
        Query query=new Query();
        query.addCriteria(Criteria.where("objectId").is(invoiceId));
        findAndModify(query,invoice,invoice.getClass(),collection);
    }
    public List<Invoice> findAllInvoices(){

        return findAll(invoice.getClass(),collection);
    }
    public List<Invoice> findByClientInvoices(Query query) {
        return findMany(query, invoice.getClass(),collection);
    }
    public boolean removeInvoice(Query query)
    {
       return removeInvoice(query,collection);
    }
    public boolean removeInvoice(Query query, CollectionEnum collection)
    {
       return remove(query,collection);
    }
}
