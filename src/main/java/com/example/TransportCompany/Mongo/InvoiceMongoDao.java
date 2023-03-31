package com.example.TransportCompany.Mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDao extends AbstractMongoDao{
    @Override
    public MongoTemplate getMongoTemplate() {
        return null;
    }

    @Override
    public void setMongoTemplate(MongoTemplate mongoTemplate) {

    }
}
