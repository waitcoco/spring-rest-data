package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    //@ManyToOne
    //private Bilu bilu;
    public Person() {

    }
}
