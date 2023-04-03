package com.example.TransportCompany.Mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MongoUtil {
    public static final String MODIFIED_BY="TransportCompany";
    private static final Logger logger = LoggerFactory.getLogger(MongoUtil.class);

    private static ObjectMapper objectMapper;
    public static <T> List<T> convertFromMongo(Collection<Document> documents, Class<T> clazz) {
        if (documents == null) {
            return Collections.emptyList();
        }

        return documents.stream()
                .map(document -> convertFromMongo(document, clazz))
                .collect(Collectors.toList());
    }
    public static Update updateAuditData(Update update) {
        final Date currentDate = new Date();

        update.setOnInsert("createdBy", MODIFIED_BY);
        update.setOnInsert("getCreationDate", currentDate);
        update.set("modifiedBy", MODIFIED_BY);
        update.set("modificationDate", currentDate);
        return update;
    }
    public static <T> T convertFromMongo(Document document, Class<T> clazz) {
        if (document == null) {
            return null;
        }
       return objectMapper.convertValue(document, clazz);
    }
    public static <T> List<Document> convertToMongo(Collection<T> objects) {
        if (objects == null) {
            return Collections.emptyList();
        }
        List<Document> collect;
        collect = objects.stream().map(MongoUtil::convertToMongo).collect(Collectors.toList());
        return collect;
    }

    public static <T> Document convertToMongo(T object){
        if (object == null) {
            return null;
        }
        Document document= new Document();
        try{
            document = Document.parse(objectMapper.writeValueAsString(object));
        }
          catch ( JsonProcessingException e)
        {
            logger.info("Exception during converting: ",e);
        }
        return document;
    }
}
