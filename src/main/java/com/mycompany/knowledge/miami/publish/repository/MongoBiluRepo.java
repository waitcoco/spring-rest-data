package com.mycompany.knowledge.miami.publish.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.in;

@Repository
public class MongoBiluRepo {

    private final MongoCollection<Document> collection;

    public MongoBiluRepo(
            MongoClient mongoClient,
            @Value("${miami.publish.mongodb.database}") String database,
            @Value("${miami.publish.mongodb.collection.bilu}") String collectionName
    ) {
        collection = mongoClient.getDatabase(database).getCollection(collectionName);
    }

    public Iterable<String> getBiluList(Iterable<String> ids) {
        return collection.find(in("_id", ids)).map(Document::toJson);
    }
}
