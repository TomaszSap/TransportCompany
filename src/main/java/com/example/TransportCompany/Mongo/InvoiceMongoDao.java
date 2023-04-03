package com.example.TransportCompany.Mongo;

import com.example.TransportCompany.model.Invoice;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class InvoiceMongoDao extends AbstractMongoDao{
    private static final CollectionEnum collection=CollectionEnum.INVOICES;
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
    public Invoice findInvoiceById(String id, Class<Invoice> entityClass){
         return (Invoice) findById(id,entityClass,collection);
    }
    public void findInvoiceAndModify(Query query, Update update, Class<Invoice> entityClass) {
         findAndModify(query,update,entityClass,collection);
    }
    public void updateInvoiceById(String id, Update update, Class<Invoice> entityClass) {
         updateById(id,update,entityClass,collection);
    }
    public List<Invoice> findAllInvoices(Class<Invoice> entityClass){
        return findAll(entityClass,collection);
    }
    public List<Invoice> findManyInvoices(Query query, Class<Invoice> entityClass) {
        return findMany(query,entityClass,collection);
    }
    public void removeInvoice(Query query)
    {
         remove(query,collection);
    }

    public void findInvoiceByClient() {
        //return f;
    }
}
