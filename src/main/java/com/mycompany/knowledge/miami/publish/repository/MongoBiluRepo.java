package com.mycompany.knowledge.miami.publish.repository;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.val;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
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

    public Iterable<String> getBiluList() {
        return collection.find().map(Document::toJson);
    }

    public List<Iterable<String>> getCaseBiluList() {
        List<Iterable<String>> cases = new ArrayList<>();
        Set<String> groupIds = new HashSet(Lists.newArrayList(collection.find().map(document->(String) document.get("groupGuid"))));
        for(val groupId: groupIds) {
            cases.add(collection.find(eq("groupGuid", groupId)).map(Document::toJson));
        }
        return cases;
    }
}
