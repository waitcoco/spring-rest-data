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
    @Column(columnDefinition = "NVARCHAR(255)")
    private String subjectId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;
    @Column(columnDefinition = "NVARCHAR(10000)")
    private String content;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String biluId;
    @Column(columnDefinition = "NVARCHAR(5000)")
    private String phones;
    @Column(columnDefinition = "NVARCHAR(255)")
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
