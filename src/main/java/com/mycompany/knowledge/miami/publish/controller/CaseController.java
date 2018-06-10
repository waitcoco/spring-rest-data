package com.mycompany.knowledge.miami.publish.controller;

import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import com.mycompany.knowledge.miami.publish.repository.CaseRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cases")
@Api(description = "Case APIs")
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

    @GetMapping("/all")
    public Iterable<Case> getAllUsers() {
        // This returns a JSON or XML with the users
        return caseRepository.findAll();
    }
}
