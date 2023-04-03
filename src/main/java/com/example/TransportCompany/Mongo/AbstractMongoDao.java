package com.example.TransportCompany.Mongo;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("rawtypes")
public abstract class AbstractMongoDao<T extends DaoModel> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMongoDao.class);
    public abstract MongoTemplate getMongoTemplate();
    public abstract void setMongoTemplate(MongoTemplate mongoTemplate);
    protected  T save(T object, CollectionEnum collectionName){
            final Document objectToSave = MongoUtil.convertToMongo(object);
            logger.debug("save to collection {}, values: {}", collectionName.getName());
            final Document result = getMongoTemplate().save(objectToSave, collectionName.getName());
            T resultObject = MongoUtil.convertFromMongo(result, (Class<T>) object.getClass());
            logger.trace("save return {}");
            return resultObject;
    }

    protected T findById(String id, Class<T> entityClass, CollectionEnum collectionName){
        logger.debug("findById for collection{}, id: {}", collectionName.getName(), id);
        final Document result = getMongoTemplate().findById(id, Document.class, collectionName.getName());
        T resultObject = MongoUtil.convertFromMongo(result, entityClass);
        logger.trace("findById return {}");
        return resultObject;
    }

    //toDo
    protected T findByClientId(String id, Class<T> entityClass, CollectionEnum collectionName)
    {
        logger.debug("findByClientId for collection{}, id: {}", collectionName.getName(), id);
        //final Document result = getMongoTemplate().f
        return null;

    }
    //todo
    protected T findAndModify(Query query, Update update, Class<T> entityClass, CollectionEnum collectionName) {
        BulkOperations bulkOps = getMongoTemplate().bulkOps(BulkOperations.BulkMode.ORDERED, Document.class, collectionName.getName());
        final FindAndModifyOptions options = new FindAndModifyOptions();
       // final Document result=getMongoTemplate().findAndModify();
        logger.debug("findAndModify for collection: {}, query: {}, update: {}", collectionName.getName(), query, update);
        return null;//result;
    }
    protected T updateById(String id, Update update, Class<T> entityClass, CollectionEnum collectionName) {
        final FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        final Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        MongoUtil.updateAuditData(update);
        logger.debug("updateById for collection: {}, id: {}, update: {}", collectionName, id, update);
        final Document result = getMongoTemplate().findAndModify(query, update, options, Document.class, collectionName.getName());
        T resultObject = MongoUtil.convertFromMongo(result, entityClass);
        logger.trace("updateById return {}", resultObject);
        return resultObject;
    }


    protected List<T> findAll(Class<T> entityClass, CollectionEnum collectionName){
        logger.debug("findAll for collection: {}", collectionName);
        final List<Document> document = getMongoTemplate().findAll(Document.class, collectionName.getName());
        logger.debug("findAll returned {} documents", document.size());
        return MongoUtil.convertFromMongo(document, entityClass);
    }

    protected List<T> findMany(Query query, Class<T> entityClass, CollectionEnum collectionName) {
        logger.debug("findMany for collection: {}, query: {}", collectionName, query);
        final List<Document> result = getMongoTemplate().find(query, Document.class, collectionName.getName());
        logger.debug("findMany returned {} documents", result.size());
        return MongoUtil.convertFromMongo(result, entityClass);
    }
    protected void remove(Query query, CollectionEnum collectionName) {
        logger.debug("remove for collection: {}, query: {}", collectionName, query);
        getMongoTemplate().remove(query, Document.class, collectionName.getName());
    }
}