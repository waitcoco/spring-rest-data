package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Bilu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    //@OneToMany(targetEntity=Person.class, mappedBy = "bilu", fetch= FetchType.EAGER)
    //private List<Person> persons;
    //@ManyToOne
    //private Case aCase;
    public Bilu (){

    }
}
