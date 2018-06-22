package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "bilu")
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Bilu {
    @Id
    @NonNull
    private String subjectId;
    private String name;
    @Column(length=5000)
    private String content;
    private String biluId;
    private String phones;
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
