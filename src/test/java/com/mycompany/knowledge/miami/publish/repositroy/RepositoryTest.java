package com.mycompany.knowledge.miami.publish.repositroy;


import com.mycompany.knowledge.miami.publish.model.gongan.Bilu;
import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import com.mycompany.knowledge.miami.publish.model.gongan.Person;
import com.mycompany.knowledge.miami.publish.repository.*;
import com.mycompany.knowledge.miami.publish.springboot.MiamiPublishApplication;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MiamiPublishApplication.class)
@ActiveProfiles("jena2gonganLocal")
public class RepositoryTest {
    @Autowired
    CaseRepository caseRepository;

    @Autowired
    BiluRepository biluRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MongoCaseBasicRepo mongoCaseBasicRepo;

    @Autowired
    PersonInfoGetter personInfoGetter;

    @Test
    //@Transactional
    public void testSaveCase() {
        Case aCase = new Case("sdddd");
        aCase.setName("测试案件");
        //aCase.setSubjectId(123l);
        Bilu bilu = new Bilu("笔录的id");
        bilu.setName("测试笔录");
        bilu.setACase(aCase);
        Person person = new Person("http://person/王大锤");
        person.setName("王大锤");
        List<Bilu> bilus = new ArrayList<Bilu>();
        bilus.add(bilu);
//        person.setBilus(bilus);
//        bilu.setPersons(Arrays.asList(new Person[]{person}));
        aCase.setBilus(bilus);
        caseRepository.save(aCase);
    }

    @Test
    @Transactional
    public void testGet() {
        for(val c : caseRepository.findAll()) {
            System.out.println(c);
        }

        for(val p: personRepository.findAll()) {
            System.out.println(p);
        }

        for(val b: biluRepository.findAll()) {
            System.out.println(b);
        }
    }

    @Test
    @Transactional
    public void testMongo(){
        mongoCaseBasicRepo.getCaseByAJBH("A3711224700002017125009");
    }

    @Test
    @Transactional
    public void testPersonInfoGetter(){

        Person person = new Person();
        try{
            person = personInfoGetter.getPersonByIdentity("420101197710219421");
        }
        catch(Exception e){
            System.out.println("error");
        }
    }
}
