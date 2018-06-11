package com.mycompany.knowledge.miami.publish.repositroy;


import com.mycompany.knowledge.miami.publish.model.gongan.Bilu;
import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import com.mycompany.knowledge.miami.publish.repository.CaseRepository;
import com.mycompany.knowledge.miami.publish.springboot.MiamiPublishApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MiamiPublishApplication.class)
@ActiveProfiles("jena2gonganDev")
public class RepositoryTest {
    @Autowired
    CaseRepository caseRepository;

    @Test
    public void testSaveCase() {
        Case aCase = new Case("sdddd");
        aCase.setName("测试案件");
        //aCase.setId(123l);
        Bilu bilu = new Bilu();
        bilu.setName("测试笔录");
        bilu.setACase(aCase);
        List<Bilu> bilus = new ArrayList<Bilu>();
        bilus.add(bilu);
        aCase.setBilus(bilus);
        caseRepository.save(aCase);
    }
}
