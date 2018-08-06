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
    private String ZBR_XM;
    private String CBDW_BH;
    private String AJBH;
    private String AJLX;
    private String AJLXName;
    private String AJMC;
    private String AJZT;
    private String AJZTName;
    private String CBDW_MC;
    private String FADD;
    private String ID;
    private String JQBH;
    private String JYAQ;
    private String LASJ;
    private String LRSJ;
    private String SLDW_MC;
    private String SLSJ;
    private String XYR_XM;
    private String ZBR_SFZH;
    @Column(columnDefinition = "text")
    private String connection;
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
