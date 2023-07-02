package com.example.TransportCompany.Mongo;

import com.example.TransportCompany.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceMongoDao extends AbstractMongoDao {
    private static final CollectionEnum collection = CollectionEnum.INVOICES;
    private MongoTemplate mongoTemplate;
    public InvoiceMongoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public Invoice saveToMongo(Invoice object) {
        return (Invoice) save(object, collection);

    }

    public Invoice findInvoiceById(String invoiceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("invoiceId").is(invoiceId));
        return (Invoice) findById(invoiceId,Invoice.class, collection);
    }


    public DaoModel findInvoiceAndModifyById(String invoiceId, Invoice invoice) {
        Query query = new Query();
        query.addCriteria(Criteria.where("invoiceId").is(invoiceId));
        return findAndModify(query, invoice, invoice.getClass(), collection);
    }

    public List<Invoice> findAllInvoices() {

        return findAll(Invoice.class, collection);
    }

    public List<Invoice> findByInvoices(Query query) {
        return findMany(query,Invoice.class, collection);
    }

    public boolean removeInvoice(Query query) {
        return removeInvoice(query, collection);
    }

    public boolean removeInvoice(Query query, CollectionEnum collection) {
        return remove(query, collection);
    }
}
