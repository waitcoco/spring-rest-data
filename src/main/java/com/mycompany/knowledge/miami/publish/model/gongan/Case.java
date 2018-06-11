package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "case_repo")
@RequiredArgsConstructor
@NoArgsConstructor
public class Case {
    @NonNull
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    //@OneToMany(targetEntity=Bilu.class, mappedBy = "aCase", fetch= FetchType.EAGER)
    @OneToMany(mappedBy = "aCase", cascade = CascadeType.ALL)
    private List<Bilu> bilus;
}
