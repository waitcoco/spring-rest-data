package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bilu")
public class Bilu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToMany(mappedBy = "bilu", cascade = CascadeType.ALL)
    private List<Person> persons;
    @ManyToOne
    @JoinColumn(name = "case_repo_id")
    private Case aCase;
    public Bilu (){

    }
}
