package com.mycompany.knowledge.miami.publish.repository;

import com.mycompany.knowledge.miami.publish.model.gongan.Relation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends CrudRepository<Relation,String> {
}
