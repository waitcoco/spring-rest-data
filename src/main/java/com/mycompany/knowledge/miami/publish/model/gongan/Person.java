package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "bilu_id")
    private Bilu bilu;
    public Person() {

    }
}
