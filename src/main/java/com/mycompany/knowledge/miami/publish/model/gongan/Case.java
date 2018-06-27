package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "case_repo",indexes = {@Index(columnList = "subjectId")})
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Case {

    @Id
    @NonNull
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String subjectId;
    private String caseId;
    private String type;
    private String name;

    private String zbrXm;
    private String cbdwBh;
    private String ajbh;
    private String ajlx;
    private String ajlxName;
    private String ajmc;
    private String ajzt;
    private String ajztName;
    private String cbdwMc;
    private String fadd;
    private String id;
    private String jqbh;
    private String jyaq;
    private String lasj;
    private String lrsj;
    private String sldwMc;
    private String slsj;
    private String xyrXm;
    private String zbrSfzh;

    // todo
    // @jinzhao

    @OneToMany(mappedBy = "aCase", cascade = CascadeType.ALL)
    private List<Bilu> bilus;
//    @ManyToMany(mappedBy = "cases", cascade = CascadeType.ALL)
//    private List<Person> psersons;
//    @Override
//    public String toString() {
//        String biluNames = String.join(",", bilus.stream().map(bilu->bilu.getName()).collect(Collectors.toList()));
//        return String.format("subjectId: %s, name: %s, bilu names: %s\n", subjectId, name, biluNames);
//    }
}
