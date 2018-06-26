package com.mycompany.knowledge.miami.publish.repository;

import com.mycompany.knowledge.miami.publish.model.gongan.Bilu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiluRepository extends CrudRepository<Bilu, String>{
}
