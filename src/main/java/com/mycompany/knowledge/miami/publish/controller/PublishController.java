package com.mycompany.knowledge.miami.publish.controller;

import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publish")
@Api(description = "publish process APIs")
public class PublishController {
    @Autowired
    PublishEngine publishEngine;
    @GetMapping("/batchProcess")
    public String publish() {
        return publishEngine.publish();
    }

    @GetMapping("/process/{modelName}")
    public void publishModel(@PathVariable("modelName") String modelName) {
    }

    @PostMapping("/ES")
    public void processBatch( @RequestBody List<String> mongoIds) {
    }
}
