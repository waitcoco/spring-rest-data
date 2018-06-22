package com.mycompany.knowledge.miami.publish.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class MongoCaseBasicRepo {
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
}
