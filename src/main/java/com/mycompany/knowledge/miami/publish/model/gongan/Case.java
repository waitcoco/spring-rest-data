package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;
import org.elasticsearch.common.recycler.Recycler;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Table(name = "case_repo")
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Case {

    @Id
    @NonNull
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String caseId;
    private String type;
    private String name;
    @OneToMany(mappedBy = "aCase", cascade = CascadeType.ALL)
    private List<Bilu> bilus;
//    @ManyToMany(mappedBy = "cases", cascade = CascadeType.ALL)
//    private List<Person> psersons;
    @Override
    public String toString() {
        String biluNames = String.join(",", bilus.stream().map(bilu->bilu.getName()).collect(Collectors.toList()));
        return String.format("id: %s, name: %s, bilu names: %s\n", id, name, biluNames);
    }
}
