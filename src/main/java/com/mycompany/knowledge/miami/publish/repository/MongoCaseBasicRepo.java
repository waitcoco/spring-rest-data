package com.mycompany.knowledge.miami.publish.repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import lombok.Data;
import lombok.val;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Data
@Repository
public class MongoCaseBasicRepo<T>{
    MongoDatabase mongoDatabase;
    Gson gson = new Gson();

    public MongoCaseBasicRepo(@Value("${mongo.mongodburi}") String mongoClientURI,
                         @Value("${mongo.mongodb}") String mongodbDatabaseName) {
        if (mongoClientURI != null && !mongoClientURI.isEmpty()) {
            MongoClientURI uri = new MongoClientURI(mongoClientURI);
            MongoClient mongoClient = new MongoClient(uri);
            mongoDatabase = mongoClient.getDatabase(mongodbDatabaseName);
        }
    }

    public Case getCaseByAJBH(String caseId){
        // todo
        // @jinzhao
        MongoCollection<Document> collection = mongoDatabase.getCollection("aj_basic");
        BasicDBObject query = new BasicDBObject("AJBH",caseId);
        FindIterable<Document> findIterable = collection.find(query);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        Case mongocase = new Case();
        while(mongoCursor.hasNext()){
            mongocase = gson.fromJson(mongoCursor.next().toJson(),Case.class);
        }
        return mongocase;
    }
}
