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
    @Column(columnDefinition = "NVARCHAR(255)")
    private String subjectId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String caseId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String type;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

//    private String zbr_xm;
//    private String cbdw_bh;

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
