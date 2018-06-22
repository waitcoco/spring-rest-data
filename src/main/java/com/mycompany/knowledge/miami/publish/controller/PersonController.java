package com.mycompany.knowledge.miami.publish.controller;

import com.mycompany.knowledge.miami.publish.exception.ResourceNotFoundException;
import com.mycompany.knowledge.miami.publish.model.gongan.Person;
import com.mycompany.knowledge.miami.publish.repository.PersonRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/persons")
@Api(description = "Person APIs")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/one")
    public Person getPerson(@RequestParam("personId") String personId){
        if(personRepository.findOne(personId)==null){
            throw new ResourceNotFoundException("person is not found");
        }
        else return personRepository.findOne(personId);
    }

    @RequestMapping("/all")
    public List<String> getPersons(){
        List<String> persons = new ArrayList<>();
        personRepository.findAll().forEach(p->persons.add(p.getName()));
        return persons;
    }
}
