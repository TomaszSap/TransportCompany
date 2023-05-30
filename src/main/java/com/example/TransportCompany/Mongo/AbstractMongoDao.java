package com.example.TransportCompany.Mongo;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.List;

@Component
@SuppressWarnings("rawtypes")
public abstract class AbstractMongoDao<T extends DaoModel> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMongoDao.class);

    public abstract void setMongoTemplate(MongoTemplate mongoTemplate);


    public abstract MongoTemplate getMongoTemplate();

    protected T save(T object, CollectionEnum collectionName) {
        final Document objectToSave = MongoUtil.convertToMongo(object);
        logger.debug("save to collection {}, values: {}", collectionName);
        final Document result = getMongoTemplate().save(objectToSave, collectionName.name());
        T resultObject = MongoUtil.convertFromMongo(result, (Class<T>) object.getClass());
        logger.trace("save return {}", resultObject);
        return resultObject;
    }

    protected T findById(String id, Class<T> entityClass, CollectionEnum collectionName) {
        logger.debug("findById for collection{}, id: {}", collectionName.getName(), id);
        final Document result = getMongoTemplate().findById(id, Document.class, collectionName.getName());
        T resultObject = MongoUtil.convertFromMongo(result, entityClass);
        logger.trace("findById return {}", resultObject);
        return resultObject;
    }

    protected T findAndModify(Query query, T object, Class<T> entityClass, CollectionEnum collectionName) {
        Update update = new Update();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null) {
                update.set(field.getName(), value);
            }
        });
        final T result = getMongoTemplate().findAndModify(query, update, entityClass);
        logger.debug("findAndModifyById for collection: {}, query: {}, update: {}", collectionName.getName(), query, update);
        return result;
    }

    protected List<T> findAll(Class<T> entityClass, CollectionEnum collectionName) {
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

    protected boolean remove(Query query, CollectionEnum collectionName) {
        logger.debug("remove for collection: {}, query: {}", collectionName, query);
        var isDeleted = getMongoTemplate().remove(query, Document.class, collectionName.getName());
        if (isDeleted.wasAcknowledged()) {
            return true;
        }
        return false;
    }
}