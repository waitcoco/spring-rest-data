package com.mycompany.knowledge.miami.publish.repository;

import com.mycompany.knowledge.miami.publish.model.gongan.IdentityRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRelationRepository extends CrudRepository<IdentityRelation,String> {
}
