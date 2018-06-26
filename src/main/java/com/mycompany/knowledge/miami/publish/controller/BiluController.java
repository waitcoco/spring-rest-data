package com.mycompany.knowledge.miami.publish.controller;

import com.mycompany.knowledge.miami.publish.exception.ResourceNotFoundException;
import com.mycompany.knowledge.miami.publish.model.gongan.Bilu;
import com.mycompany.knowledge.miami.publish.repository.BiluRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/bilus")
@Api(description = "Bilu APIs")
public class BiluController {
    @Autowired
    BiluRepository biluRepository;

    @Transactional
    @RequestMapping("/one")
    public Bilu getBilu(@RequestParam("biluId") String biluId){
        if(biluRepository.findOne(biluId)==null){
            throw new ResourceNotFoundException("bilu is not found");
        }
        else return biluRepository.findOne(biluId);
    }

    @Transactional
    @RequestMapping("/all")
    public List<String> getBilus(){
        List<String> bilus = new ArrayList<>();
        biluRepository.findAll().forEach(b->bilus.add(b.getName()));
        return bilus;
    }

}
