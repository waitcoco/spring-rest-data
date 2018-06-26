package com.mycompany.knowledge.miami.publish.repository;

import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends CrudRepository<Case, String>{
//    @Query("from CaseBase aCase where aCase.caseName=:caseName")
//    CaseBase findByName(String caseName);
}
