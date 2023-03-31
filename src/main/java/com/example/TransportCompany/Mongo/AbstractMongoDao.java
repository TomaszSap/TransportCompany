package Mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public abstract class AbstractMongoDao<T> {
    private static final int HOUR_ERROR_OFFSET = 2;
    private static final Logger logger = LoggerFactory.getLogger(AbstractMongoDao.class);
    public abstract MongoTemplate getMongoTemplate();
    public abstract void setMongoTemplate(MongoTemplate mongoTemplate);
    protected  T save(T object, CollectionEnum collectionName){
        if (object.getObjectId() == null) {
            final Document objectToSave = MongoUtil.convertToMongo(object);
            logger.debug("save to collection {}, values: {}", collectionName.getName());
            final Document result = getMongoTemplate().save(objectToSave, collectionName.getName());
            T resultObject = MongoUtil.convertFromMongo(result, (Class<T>) object.getClass());
            logger.trace("save return {}");
            return resultObject;
        }
        return replaceById(object, collectionName);
    }


    protected void save(List<T> objects, CollectionEnum collectionName) {
        if (objects == null) {
            return;
        }
        saveWithInserts(objects, collectionName);
        saveWithUpdates(objects, collectionName);
    }

    private void saveWithUpdates(List<T> objects, CollectionEnum collectionName) {
        List<T> objectsToUpdate = objects.stream().filter(item -> item.getObjectId() != null).collect(Collectors.toList());
        objectsToUpdate.forEach(item -> save(item, collectionName));
    }

    private void saveWithInserts(List<T> objects, CollectionEnum collectionName) {
        List<T> objectsToInsert = objects.stream().filter(item -> item.getObjectId() == null).collect(Collectors.toList());
        List<Document> documentsToInsert = objectsToInsert.stream().map(MongoUtil::convertToMongo).collect(Collectors.toList());
        logger.debug("saveWithInserts to collection {} document number  {}", collectionName.getName(), documentsToInsert.size());
        getMongoTemplate().insert(documentsToInsert, collectionName.toString().toLowerCase());
    }

    protected void updateCollection(List<T> collection, String collectionName, String collectionPath, String collectionIdColumn, String collectionId){
        if (collectionIdColumn==null){
            throw new RuntimeException("Missing parameter " + collectionIdColumn);
        }
        if (collectionName==null){
            throw new RuntimeException("Missing parameter " + collectionName);
        }
        if (collectionPath==null){
            throw new RuntimeException("Missing parameter " + collectionPath);
        }
        if (collection!=null){
            Query query = Query.query(Criteria.where(collectionIdColumn).is(collectionId));
            Update update = new Update().push(collectionPath).each(collection);
            logger.debug("updateCollection  on collection {}, update {}, for query {}", collectionName, update, query);
            UpdateResult result = getMongoTemplate().updateFirst(query, update, collectionName);
            if (result.getModifiedCount() < 1){
                throw new RuntimeException("Update failed. No records affected!");
            }
        }
    }

    protected T replaceById(T object, CollectionEnum collectionName) {
        final Document objectToUpdate = MongoUtil.convertToMongo(object);
        final Query query = new Query();
        final ObjectId objectId = new ObjectId(object.getObjectId());
        query.addCriteria(objectId);

        final FindAndReplaceOptions options = new FindAndReplaceOptions();
        options.returnNew();

        logger.debug("replaceById on collection {} for query {}, object: {}", collectionName.getName(), query);
        final Document result = getMongoTemplate().findAndReplace(query, objectToUpdate, options, Document.class, collectionName.getName());
        T resultObject = MongoUtil.convertFromMongo(result, (Class<T>) object.getClass());
        logger.trace("replaceById return {}");
        return resultObject;
    }
    protected T findById(String id, Class<T> entityClass, CollectionEnum collectionName){
        logger.debug("findById for collection{}, id: {}", collectionName.getName(), id);
        final Document result = getMongoTemplate().findById(id, Document.class, collectionName.getName());
        T resultObject = MongoUtil.convertFromMongo(result, entityClass);
        logger.trace("findById return {}");
        return resultObject;
    }
    @Transactional
    protected T findOne(Query query, Class<T> entityClass, CollectionEnum collectionName) {
        logger.debug("findOne for collection: {}, query: {}", collectionName.getName(), query);
        final Document result = getMongoTemplate().findOne(query, Document.class, collectionName.getName());
        T resultObject = MongoUtil.convertFromMongo(result, entityClass);
        logger.trace("findOne return {}", getObjectAnonymizedSerialization(resultObject));
        return resultObject;
    }

    protected boolean exists(Query query, CollectionEnum collectionName) {
        // Not working with ReplicaSet & spring-data-mongodb (version 2) !!!
        // return mongoTemplate.exists(query, collectionName);
        logger.debug("exists for collection: {}, query: {}", collectionName.getName(), query);
        boolean result = getMongoTemplate().findOne(query, Document.class, collectionName.getName()) != null;
        logger.debug("exists result {}", result);
        return result;
    }
    protected T findAndModify(Query query, Update update, Class<T> entityClass, CollectionEnum collectionName) {
        BulkOperations bulkOps = getMongoTemplate().bulkOps(BulkOperations.BulkMode.ORDERED, Document.class, collectionName.getName());
        final FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        MongoUtil.updateAuditData(update);
        logger.debug("findAndModify for collection: {}, query: {}, update: {}", collectionName.getName(), query, update);
        bulkOps.updateOne(query,update);
        // final var result =getMongoTemplate().upsert(query, update,Document.class, collectionName.getName());
        // T resultObject = MongoUtil.convertFromMongo(result, entityClass);
        // logger.trace("findAndModify return {}", resultObject);
        bulkOps.execute();
        return null;
    }
    protected T updateById(String id, Update update, Class<T> entityClass, String collectionName) {
        final FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        final Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        MongoUtil.updateAuditData(update);
        logger.debug("updateById for collection: {}, id: {}, update: {}", collectionName, id, update);
        final Document result = getMongoTemplate().findAndModify(query, update, options, Document.class, collectionName);
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

    protected long countDocuments(Query query, CollectionEnum collectionName){
        logger.debug("countDocuments for collection: {}, query: {}", collectionName, query);
        long result =  getMongoTemplate().count(query, collectionName.getName());
        logger.debug("countDocuments returned {}", result);
        return result;
    }


    protected void updateMany(Query query, Update update, CollectionEnum collectionName) {
        logger.debug("updateMany for collection: {}, query: {}, update: {}", collectionName, query, update);
        getMongoTemplate().updateMulti(query, MongoUtil.updateAuditData(update), collectionName.getName());
    }

    protected void remove(Query query, CollectionEnum collectionName) {
        logger.debug("remove for collection: {}, query: {}", collectionName, query);
        getMongoTemplate().remove(query, Document.class, collectionName.getName());
    }
    public Criteria getDateEqualsCriteria(String field, Date date) {
        //use for equals comparisonon date object
        //hour offset is to reduce impact of changing timezone
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -HOUR_ERROR_OFFSET);
        Date dateFrom = cal.getTime();
        cal.setTime(date);
        cal.add(Calendar.HOUR, HOUR_ERROR_OFFSET);
        Date dateTo = cal.getTime();
        return Criteria.where(field).gte(dateFrom).lte(dateTo);
    }

}