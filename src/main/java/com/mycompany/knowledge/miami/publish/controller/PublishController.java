package com.mycompany.knowledge.miami.publish.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.knowledge.miami.publish.engine.EsPublishEngine;
import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import com.mycompany.knowledge.miami.publish.repository.MongoBiluRepo;
import io.swagger.annotations.Api;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/publish")
@Api(description = "publish process APIs")
public class PublishController {
    @Autowired
    PublishEngine publishEngine;

    @Autowired
    EsPublishEngine esPublishEngine;

    @Autowired
    MongoBiluRepo mongoBiluRepo;

    @GetMapping("/batchProcess")
    public String publish() {
        return publishEngine.publish();
    }

    @GetMapping("/process/{modelName}")
    public void publishModel(@PathVariable("modelName") String modelName) {
    }

    @PostMapping("/ES")
    public void processBatch( @RequestBody List<String> mongoIds) throws IOException {
        //uploadBiluToEs(mongoIds);
        uploadCaseToEs(mongoIds);
    }

    @PostMapping("/ES/full")
    public void processBatch() throws IOException {
        //uploadBiluToEs(null);
        clearES();
        uploadCaseToEs(null);
    }

    @PostMapping("/ES/clear")
    public void clearES() throws IOException {
        if (esPublishEngine.indexExists()) {
            esPublishEngine.deleteIndex();
        }
    }

    private void uploadCaseToEs(List<String> mongoIds) throws IOException {
        if (!esPublishEngine.indexExists()) {
            esPublishEngine.createIndex();
        }

        val caseList = mongoBiluRepo.getCaseBiluList();

        for(val aCase: caseList) {
            JsonObject jsonObject = new JsonObject();
            String caseId = null, caseName = null, caseGuid = null;
            val iter = aCase.iterator();

            JsonArray arr = new JsonArray();

            while(iter.hasNext()) {
                val json = new JsonParser().parse(iter.next()).getAsJsonObject();

                if(json.has("groupName")) {
                    caseName = json.get("groupName").getAsString();
                }
                caseGuid = json.get("groupGuid").getAsString();

                if(json.has("groupId")) {
                    caseId = json.get("groupId").getAsString();
                }

                arr.add(json);
            }
            jsonObject.addProperty("caseId", caseId);
            jsonObject.addProperty("caseName", caseName);
            jsonObject.addProperty("caseGuid", caseGuid);

            jsonObject.add("bilus", arr);

            try {
                esPublishEngine.addJsonDocument(caseGuid, jsonObject.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        esPublishEngine.flush();
    }

    private void uploadBiluToEs(List<String> mongoIds) throws IOException {
        if (!esPublishEngine.indexExists()) {
            esPublishEngine.createIndex();
        }

        val biluList = mongoIds == null ? mongoBiluRepo.getBiluList() : mongoBiluRepo.getBiluList(mongoIds);

        biluList.forEach(bilu -> {
            val json = new JsonParser().parse(bilu).getAsJsonObject();
            val id = json.get("_id").getAsString();
            json.remove("_id");
            try {
                esPublishEngine.addJsonDocument(id, json.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        esPublishEngine.flush();
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, this is publish service";
    }
}
