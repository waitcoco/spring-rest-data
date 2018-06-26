package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "bilu",indexes = {@Index(columnList = "subjectId")})
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Bilu {
    @Id
    @NonNull
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String subjectId;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String name;
    @Column(columnDefinition = "NVARCHAR2(10000)")
    private String content;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String biluId;
    @Column(columnDefinition = "NVARCHAR2(5000)")
    private String phones;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String bankcards;
//    @ManyToMany(mappedBy = "bilus", cascade = CascadeType.ALL)
//    private List<Person> persons;
    @ManyToOne
    @JoinColumn(name = "case_repo_subject_id")
    private Case aCase;
//    @Override
//    public String toString() {
//        String personNames = String.join(",", persons.stream().map(person->person.getName()).collect(Collectors.toList()));
//        return String.format("subjectId: %s, name: %s, person names: %s\n", subjectId, name, personNames);
//    }
}
