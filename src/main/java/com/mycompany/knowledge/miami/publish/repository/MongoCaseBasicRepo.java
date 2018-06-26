package com.mycompany.knowledge.miami.publish.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Repository
public class MongoCaseBasicRepo {
    @Autowired
    private MongoTemplate mongoTemplate;
    private final MongoCollection<Document> collection;

    public MongoCaseBasicRepo(
            MongoClient mongoClient,
            @Value("${miami.publish.mongocase.database}") String database,
            @Value("${miami.publish.mongocase.collection.bilu}") String collectionName
    ) {
        collection = mongoClient.getDatabase(database).getCollection(collectionName);
    }

    public Iterable<String> getCaseList() {
        return collection.find().map(Document::toJson);
    }

    public Case getCaseByAJBH(String caseId){
        // todo
        // @jinzhao
        Query query = new Query();
        query.addCriteria(new Criteria("caseId").is(caseId));
        Case aCase = this.mongoTemplate.findOne(query,Case.class);
        return aCase;
    }
}
