package com.mycompany.knowledge.miami.publish.controller;

import com.mycompany.knowledge.miami.publish.exception.ResourceNotFoundException;
import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import com.mycompany.knowledge.miami.publish.repository.CaseRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cases")
@Api(description = "CaseBase APIs")
public class CaseController {
    @Autowired
    CaseRepository caseRepository;

    @PostMapping("/add") // Map ONLY GET Requests
    public String addNewCase (@RequestBody Case c) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        caseRepository.save(c);
        return "Saved";
    }

    @Transactional
    @RequestMapping("/one")
    public Case getPerson(@RequestParam("_id") String caseId){
        if(caseRepository.findOne(caseId)==null){
            throw new ResourceNotFoundException("person is not found");
        }
        else return caseRepository.findOne(caseId);
    }

    @GetMapping("/all")
    @Transactional
    public List<String> getAllCases() {
        // This returns a JSON or XML with the users
        List<String> cases = new ArrayList<>();
        caseRepository.findAll().forEach(c->cases.add(c.getName()));
        return cases;
    }

    @Transactional
    @GetMapping("/size")
    public long getSize() {
     return caseRepository.count();
    }

}
