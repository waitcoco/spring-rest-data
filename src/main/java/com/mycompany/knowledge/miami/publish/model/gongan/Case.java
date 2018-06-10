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
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caseName;
    //@OneToMany(targetEntity=Bilu.class, mappedBy = "aCase", fetch= FetchType.EAGER)
    //private List<Bilu> bilus;
    public Case() {

    }
}
